package de.fau.osr.core.vcs.base;

import java.io.File;
import java.util.Objects;


/**
 * This entity class holds the properties of a committed file.
 * @author Gayathery
 */

public class CommitFile {

    public CommitState commitState;
    public final File workingCopy;
    public final File oldPath;
    public final File newPath;
    public final String commitID;
    public final String changedData;
    public float impact;

    /**
     * @author Gayathery
     * @param workingCopy the root directory of the working copy of the vcs
     * @param oldPath
     * @param newPath
     * @param commitState
     * @param commitID
     */
    public CommitFile(File workingCopy, File oldPath, File newPath, CommitState commitState, String commitID,String changedData)
    {
    	this.workingCopy = workingCopy;
        this.commitState = commitState;
        this.oldPath = oldPath;
        this.newPath = newPath;
        this.commitID = commitID;
        this.changedData = changedData;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof CommitFile)) return false;
        CommitFile ocf = (CommitFile) o;
        return commitState.equals(ocf.commitState) && oldPath.equals(ocf.oldPath) && newPath.equals(ocf.newPath) && commitID.equals(ocf.commitID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commitState, oldPath, newPath, commitID);
    }
}
