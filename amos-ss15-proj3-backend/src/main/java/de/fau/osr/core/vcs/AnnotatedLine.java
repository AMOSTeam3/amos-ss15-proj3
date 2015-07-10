/*
 * This file is part of Req-Tracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * Req-Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Req-Tracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Req-Tracker.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fau.osr.core.vcs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.eclipse.jgit.lib.ObjectId;

import de.fau.osr.core.db.DataSource;

public class AnnotatedLine {
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((line == null) ? 0 : line.hashCode());
        result = prime * result
                + ((requirements == null) ? 0 : requirements.hashCode());
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
        AnnotatedLine other = (AnnotatedLine) obj;
        if (line == null) {
            if (other.line != null)
                return false;
        } else if (!line.equals(other.line))
            return false;
        if (requirements == null) {
            if (other.requirements != null)
                return false;
        } else if (!requirements.equals(other.requirements))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AnnotatedLine [requirements=" + requirements + ", line="
                + line + "]";
    }

    public AnnotatedLine(Collection<String> requirements, String line) {
        this.requirements = requirements;
        this.line = line;
    }
    
    /**
     * Convert AnnotatedWords to a list of AnnotatedLines.
     * Currently all annotations of a type that is different from ObjectID are
     * silently discarded. All annotations that are of type ObjectId are looked
     * up and converted via dataSource.
     * @param words
     * @param dataSource
     * @return
     * @throws IOException
     */
    public static List<AnnotatedLine> wordsToLine(AnnotatedWords words, DataSource dataSource) throws IOException {
    	List<AnnotatedLine> res = new ArrayList<>(words.source.realLines.size());
		HashSet<String> requirements = new HashSet<>();
		int curLine = 0;
    	for(int i=0; i<words.source.size(); ++i) {
    		for(Object o : words.annotations[i]) {
    			if(o instanceof ObjectId) {
    				requirements.addAll(dataSource.getReqRelationByCommit(((ObjectId) o).getName()));
    			}
    		}
    		if(i + 1 == words.source.size() ||
    				words.source.getLineByWord(i) != words.source.getLineByWord(i+1)) {
    			AnnotatedLine line = new AnnotatedLine(new ArrayList<>(requirements), words.source.getLine(curLine));
    			++curLine;
	    		res.add(line);
	    		if(i + 1 != words.source.size())
	    			requirements = new HashSet<>();
    		}
    	}
    	return res;
    }

    private Collection<String> requirements;

    /**
     * @return all associated requirements
     */
    public Collection<String> getRequirements() {
        return requirements;
    }

    private String line;

    /**
     * @return one line of code
     */
    public String getLine() {
        return line;
    }
}
