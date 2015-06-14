package de.fau.osr.util.sorting;


import de.fau.osr.gui.Model.DataElements.CommitFile;

import java.util.Comparator;

/**
 * Sort-Strategy for I_Collection_Model to sort files list by their file name.
 * @author: Taleh Didover
 */
public class SortByFilename implements Comparator<CommitFile> {
    @Override
    public int compare(CommitFile lhs, CommitFile rhs) {
        return lhs.newPath.compareTo(rhs.newPath);
    }
}

