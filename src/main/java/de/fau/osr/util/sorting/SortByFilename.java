package de.fau.osr.util.sorting;

import java.util.Comparator;

/*
 * Sort-Strategy for GuiModel to sort Files list by filename
 * @author: Taleh Didover
 */
public class SortByFilename implements Comparator<String> {
    @Override
    public int compare(String fnameLhs, String fnameRhs) {
        return fnameLhs.compareTo(fnameRhs);
    }
}

