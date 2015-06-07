package de.fau.osr.util.filtering;

import com.google.common.base.Predicate;

import de.fau.osr.gui.Model.DataElements.Requirement;

/**
 * Filter-Strategy to filter/find a given Requirement-ID (String).
 * Empty-String means no filtering.
 * @author: Taleh Didover
 */
public class FilterByExactString implements Predicate<Requirement> {
    private String id;

    public FilterByExactString() {
        this("");
    }
    
    public FilterByExactString(String id) {
        this.id = id;
    }
    
    public FilterByExactString(Requirement req) {
        if(req == null){
            id = "";
        }
        id = req.getID();
    }

    @Override
    public boolean apply(Requirement input) {
     // if findString is empty, then no filtering.
        return id.isEmpty() || id.equals(input.getID());
    }
}
