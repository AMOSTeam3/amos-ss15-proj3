package de.fau.osr.util;

import java.text.BreakIterator;
import java.util.ArrayList;

public class FlatSource {
	/**
	 * @param source
	 * @param lineNumbers
	 */
	public FlatSource(String source, Integer[] lineNumbers) {
		this.source = source;
		this.lineNumbers = lineNumbers;
	}
	/**
	 * the broken up String
	 */
	public String source;
	/**
	 * for each line of the String source, its zero based line number
	 */
	public Integer[] lineNumbers;
	
    
    /**
     * Breaks a string at every word boundary to enable fine grained diffs
     * @param source
     * @return
     */
    public static FlatSource flatten(String source) {
    	/*
    	 *  wordIterator could eventually be changed to a BreakIterator that is
    	 *  better suited to source code than BreakIterator.getWordInstance().
    	 */
    	BreakIterator wordIterator = BreakIterator.getWordInstance();
		wordIterator.setText(source);
	    int start = wordIterator.first();
	    int end = wordIterator.next();
    	int curLine = 0;
    	ArrayList<Integer> lineNumbers = new ArrayList<>();
    	StringBuilder sb = new StringBuilder();

	    while (end != BreakIterator.DONE) {
	        String word = source.substring(start,end);
	        
	        //replace lines with nothing but spaces by newline
	        if(word.contains("\n")) word = "\n";
	        if(!word.equals("\n")) {
	        	sb.append(word + "\n");
	        	lineNumbers.add(curLine);
	        } else {
	        	//sb.append("\n");
	        	//lineNumbers.add(curLine);
	        }
	        if(word.charAt(0) == '\n') ++curLine;
	        start = end;
	        end = wordIterator.next();
	    }
		return new FlatSource(sb.toString(), lineNumbers.toArray(new Integer[0]));
    }
}