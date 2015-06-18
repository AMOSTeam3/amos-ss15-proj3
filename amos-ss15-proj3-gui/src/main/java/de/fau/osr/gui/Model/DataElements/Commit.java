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
    public String id;
    public String message;
    public List<CommitFile> files;

    public Commit(String id, String message, List<CommitFile> files) {
        this.id = id;
        this.message = message;
        this.files = files;
    }

    public Commit(de.fau.osr.core.domain.Commit commit) {
        this.id = commit.getId();
        this.message = commit.getMessage();
        files = new ArrayList<>();

        List<de.fau.osr.core.vcs.base.CommitFile> commitFilesToCopy = commit.getCommitFiles();

        if (commitFilesToCopy != null) {
            for (de.fau.osr.core.vcs.base.CommitFile commitFile : commitFilesToCopy) {
                files.add(new CommitFile(commitFile));
            }
        }

    }

    @Override
    public Presenter visit(Visitor visitor) {
        return visitor.toPresenter(this);
    }

}
