package de.fau.osr.util.filtering;

import com.google.common.base.Predicate;

/**
 * Filter-Strategy to filter/find a given Requirement-ID (String).
 * Empty-String means no filtering.
 * @author: Taleh Didover
 */
public class FilterByExactString implements Predicate<String> {
    private String findString;

    public FilterByExactString() {
        this(null);
    }

    public FilterByExactString(String exactString) {
        if (exactString == null)
            exactString = "";

        this.findString = exactString;
    }

    @Override
    public boolean apply(String s) {
        // if findString is empty, then no filtering.
        return findString.isEmpty() || findString.equals(s);
    }
}
