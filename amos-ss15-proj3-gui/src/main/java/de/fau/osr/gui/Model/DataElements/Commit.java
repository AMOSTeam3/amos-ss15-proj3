package de.fau.osr.gui.Model.DataElements;

import de.fau.osr.gui.Controller.Visitor;
import de.fau.osr.gui.View.Presenter.Presenter;

import java.util.ArrayList;
import java.util.List;

/**
 * This Class is a Container for all information related to one commit.
 * @author: Florian Gerdes
 */
public class Commit extends DataElement {

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((files == null) ? 0 : files.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime
                * result
                + ((instanceRequirement == null) ? 0 : instanceRequirement
                        .hashCode());
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Commit other = (Commit) obj;

        if (files == null) {
            if (other.files != null)
                return false;
        } else if (!files.equals(other.files))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        // TOOD Somehow "instanceRequirement" is not correctly defined.
//        if (instanceRequirement == null) {
//            if (other.instanceRequirement != null)
//                return false;
//        } else if (!instanceRequirement.equals(other.instanceRequirement))
//            return false;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        return true;
    }

    public String id;
    public String message;
    public Requirement instanceRequirement;
    public List<CommitFile> files;

    public Commit(String id, String message, List<CommitFile> files) {
        this.id = id;
        this.message = message;
        this.files = files;
        this.instanceRequirement = null;
    }

    public Commit(de.fau.osr.core.vcs.base.Commit commit) {
        this.id = commit.getId();
        this.message = commit.getMessage();
        files = new ArrayList<>();
        this.instanceRequirement = null;

    }

    @Override
    public Presenter visit(Visitor visitor) {
        return visitor.toPresenter(this);
    }
}
