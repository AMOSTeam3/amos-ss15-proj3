package de.fau.osr.util;

import java.nio.ByteBuffer;

import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.util.IntList;

/**
 * FlatSource is a RawText that has been broken into a single line per word.
 * @author tobias
 */
public class FlatSource extends RawText {
	/**
	 * Maps (zero based) line numbers in unbroken file to start bytes
	 */
	public IntList realLines;
	
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
}