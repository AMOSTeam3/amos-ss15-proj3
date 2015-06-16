package de.fau.osr.gui.Model.DataElements;

import de.fau.osr.core.vcs.base.CommitState;
import de.fau.osr.gui.Controller.Visitor;
import de.fau.osr.gui.View.Presenter.Presenter;

import java.io.File;


/**
 * This entity class holds the properties of a committed file.
 *
 * @author Gayathery
 */


public class CommitFile extends DataElement {

    public CommitState commitState;
    public final File workingCopy;
    public final File oldPath;
    public final File newPath;
    public final String commitID;
    public final String changedData;
    public float impact;

    /**
     * @param workingCopy the root directory of the working copy of the vcs
     * @param oldPath
     * @param newPath
     * @param commitState
     * @param commitID
     * @author Gayathery
     */
    public CommitFile(File workingCopy, File oldPath, File newPath, CommitState commitState, String commitID, String changedData) {
        this.workingCopy = workingCopy;
        this.commitState = commitState;
        this.oldPath = oldPath;
        this.newPath = newPath;
        this.commitID = commitID;
        this.changedData = changedData;
    }


    public CommitFile(de.fau.osr.core.vcs.base.CommitFile commitFile) {
        this.workingCopy = commitFile.workingCopy;
        this.oldPath = commitFile.oldPath;
        this.newPath = commitFile.newPath;
        this.commitID = commitFile.commitID;
        this.changedData = commitFile.changedData;
        this.commitState = commitFile.commitState;
        this.impact = commitFile.impact;
    }



    @Override
    public Presenter visit(Visitor visitor) {
        return visitor.toPresenter(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommitFile)) return false;

        CommitFile that = (CommitFile) o;

        if (commitState != that.commitState) return false;
        if (workingCopy != null ? !workingCopy.equals(that.workingCopy) : that.workingCopy != null) return false;
        if (oldPath != null ? !oldPath.equals(that.oldPath) : that.oldPath != null) return false;
        if (newPath != null ? !newPath.equals(that.newPath) : that.newPath != null) return false;
        if (!commitID.equals(that.commitID)) return false;
        return !(changedData != null ? !changedData.equals(that.changedData) : that.changedData != null);

    }

    @Override
    public int hashCode() {
        int result = commitState != null ? commitState.hashCode() : 0;
        result = 31 * result + (workingCopy != null ? workingCopy.hashCode() : 0);
        result = 31 * result + (oldPath != null ? oldPath.hashCode() : 0);
        result = 31 * result + (newPath != null ? newPath.hashCode() : 0);
        result = 31 * result + commitID.hashCode();
        result = 31 * result + (changedData != null ? changedData.hashCode() : 0);
        return result;
    }
}
