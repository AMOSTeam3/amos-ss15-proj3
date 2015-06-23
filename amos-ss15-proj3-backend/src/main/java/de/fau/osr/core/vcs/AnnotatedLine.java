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
    
    public static List<AnnotatedLine> wordsToLine(AnnotatedWords words, DataSource dataSource) throws IOException {
    	List<AnnotatedLine> res = new ArrayList<>();
    	String[] splitWords = words.source.source.split("\n");
    	if(words.annotations.length == 0) return Collections.emptyList();
    	StringBuilder line = new StringBuilder();
    	List<String> requirements = new ArrayList<>();
    	int curLine = 0;
    	for(int i=0; i<splitWords.length; ++i) {
    		while(curLine < words.source.lineNumbers[i]) {
    			res.add(new AnnotatedLine(new HashSet<>(requirements), line.toString()));
    			requirements = new ArrayList<>();
    			line.setLength(0);
    			++curLine;
    		}
    		line.append(splitWords[i]);;
    		for(Object o : words.annotations[i]) {
    			if(o instanceof ObjectId) {
    				requirements.addAll(dataSource.getReqRelationByCommit(((ObjectId) o).getName()));
    			}
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
