package de.fau.osr.gui.Model.DataElements;

import de.fau.osr.gui.Controller.Visitor;
import de.fau.osr.gui.View.Presenter.Presenter;

import java.io.File;
import java.util.Objects;


/**
 * This entity class holds the properties of a committed file.
 * @author Gayathery
 */


public class CommitFile extends DataElement{
    
    public enum CommitState {
        ADDED,
        MODIFIED,
        DELETED,
        RENAMED,
        COPIED
    }

    public CommitState commitState;
    public File oldPath;
    public File newPath;
    public String commitID;
    public String changedData;
    public float impact;

    /**
     * @author Gayathery
     * @param oldPath
     * @param newPath
     * @param commitState
     * @param commitID
     */
    public CommitFile(File oldPath, File newPath, CommitState commitState, String commitID,String changedData)
    {
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
    
    @Override
    public Presenter visit(Visitor visitor) {
        return visitor.toPresenter(this);
    }
}
