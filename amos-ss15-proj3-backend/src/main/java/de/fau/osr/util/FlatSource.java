/*
 * This file is part of ReqTracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * ReqTracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ReqTracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with ReqTracker.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fau.osr.util;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.util.IntList;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * FlatSource is a RawText that has been broken into a single line per word.
 * @author tobias
 */
public class FlatSource extends RawText {
	/**
	 * Maps (zero based) line numbers in unbroken file to start bytes
	 */
	final public IntList realLines;
	
	/**
     * Breaks a string at every word boundary to enable fine grained diffs
     * @param source
     */
    private FlatSource(byte[] input, IntList realLines) {
    	//binary files are useless here, so just set them empty
		super(input);
		this.realLines = realLines;
	}
    
    public static FlatSource flatten(byte[] input) {
    	// file is binary, ignore all contents
    	if(RawText.isBinary(input)) return new FlatSource(new byte[0], new IntList());
    	
    	IntList realLines = new IntList();
    	ByteBuffer to = ByteBuffer.allocate(input.length*2), from = ByteBuffer.wrap(input);
    	boolean atLineStart = true, lastWord = false, lastSpace = false;
    	while(from.hasRemaining()) {
    		if(atLineStart) {
    			realLines.add(to.position());
    		}
    		byte cur = from.get();
    		if (cur == '\n') {
    			atLineStart = true;
    			to.put(cur);
    		} else if (cur == ' ' || cur == '\t' ) {
    			if(!atLineStart && !lastSpace)
    				to.put((byte)'\n');
        		to.put(cur);
        		lastSpace = true;
        		lastWord = false;
    			atLineStart = false;
    		} else {
    			if(!atLineStart && !lastWord)
    				to.put((byte)'\n');
    			to.put(cur);
    			lastSpace = false;
    			lastWord = true;
    			atLineStart = false;
    		}
    	}
    	byte[] out = new byte[to.position()];
    	to.position(0);
    	to.get(out);
    	return new FlatSource(out, realLines);//new FlatSource(new byte[])
    }
    
    /**
     * Returns the line number a given (zero indexed) byte is part of
     * @param byteIndex
     * @return
     */
    public int getLineByByte(int byteIndex) {
    	int low = 0, high = realLines.size();
    	while(high - low > 1) {
    		int mid = (high + low)/2;
    		if(realLines.get(mid) > byteIndex) high = mid;
    		else low = mid;
    	}
    	return low;
    }
    
    /**
     * Returns the byte index into this FlatSource of the word at rank wordIndex
     * @param wordIndex
     * @return
     */
    public int getByte(int wordIndex) {
    	return lines.get(wordIndex + 1);
    }
    
    /**
     * returns the real line number of a given word
     * @param wordIndex
     * @return
     */
    public int getLineByWord(int wordIndex) {
    	return getLineByByte(getByte(wordIndex));
    }
    
    /**
     * reassemble one whole line and parse it into a string
     * @param line zero based line index
     * @return
     */
    public String getLine(int line) {
    	int offset = realLines.get(line);
    	int length;
    	if(realLines.size() > line + 1)
    		length = realLines.get(line+1) - offset;
    	else
    		length = content.length - offset;
    	return new String(content, offset, length).replace("\n","");
    }
    
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	for(int i=0; i<realLines.size(); ++i) {
    		sb.append(getLine(i) + "\n");
    	}
    	return sb.toString();
    }
    
    private static class ObjectKey {
    	public ObjectKey(Function<ObjectId, byte[]> fun, ObjectId id) {
			this.fun = fun;
			this.id = id;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ObjectKey other = (ObjectKey) obj;
			if(fun != other.fun) return false;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}
		final Function<ObjectId, byte[]> fun;
    	final ObjectId id;
    }
    
    static private LoadingCache<ObjectKey, FlatSource> flatCache = CacheBuilder.newBuilder()
    		.maximumSize(10)
    		.build(new CacheLoader<ObjectKey, FlatSource>() {
				@Override
				public FlatSource load(ObjectKey key) throws Exception {
					return flatten(key.fun.apply(key.id));
				}
    		});
    
	/**
	 * Either calls flatten(input.apply(idAfter)) or returns a cached FlatSource.
	 * For the caching mechanism to be consistent, idAfter and the address of
	 * input have to uniquely identify the contents of the file (trivial for git).
	 * @param input
	 * @param idAfter
	 * @return
	 */
	public static FlatSource flatten(Function<ObjectId, byte[]> input, ObjectId idAfter) {
		try {
			return flatCache.get(new ObjectKey(input, idAfter));
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
}