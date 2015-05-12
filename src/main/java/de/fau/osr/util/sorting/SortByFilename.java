package de.fau.osr.util.sorting;

import de.fau.osr.core.vcs.base.CommitFile;

import java.util.Comparator;

/*
 * Sort-Strategy for GuiModel to sort files list by their file name.
 * @author: Taleh Didover
 */
public class SortByFilename implements Comparator<CommitFile> {
    @Override
    public int compare(CommitFile lhs, CommitFile rhs) {
        return lhs.oldPath.compareTo(rhs.oldPath);
    }
}

