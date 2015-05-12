package de.fau.osr.util.sorting;

import java.util.Comparator;

/*
 * Sort-Strategy for GuiModel to sort by Files chronically.
 * @author: Taleh Didover
 */
public class SortByChronic implements Comparator<String> {
    @Override
    public int compare(String fnameLhs, String fnameRhs) {
        return fnameLhs.compareTo(fnameRhs);
    }
}
