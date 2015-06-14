package de.fau.osr.util.sorting;


import de.fau.osr.gui.Model.DataElements.CommitFile;

import java.util.Comparator;

/**
 * Sort-Strategy for I_Collection_Model to sort files by commit id (=> chronological).
 * @author: Taleh Didover
 */
public class SortByCommitID implements Comparator<CommitFile> {
    @Override
    public int compare(CommitFile lhs, CommitFile rhs) {
        return lhs.commitID.compareToIgnoreCase(rhs.commitID);
    }
}
