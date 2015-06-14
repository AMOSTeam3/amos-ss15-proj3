package de.fau.osr.gui.util.filtering;

import com.google.common.base.Predicate;
import de.fau.osr.gui.Model.DataElements.Requirement;

/**
 * Filter-Strategy to filter/find a given Requirement-ID (String).
 * Empty-String means no filtering.
 * @author: Taleh Didover
 */
public class FilterByExactString implements Predicate<Requirement> {
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
    public boolean apply(Requirement req) {
        // if findString is empty, then no filtering.
        return findString.isEmpty() || findString.equals(req.getID());
    }
}
