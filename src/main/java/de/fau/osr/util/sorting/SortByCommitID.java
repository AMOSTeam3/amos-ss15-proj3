package de.fau.osr.util.sorting;



import java.util.Comparator;

import de.fau.osr.gui.Model.DataElements.CommitFile;

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
