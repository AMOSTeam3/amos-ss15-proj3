package de.fau.osr.gui.Model.DataElements;

import de.fau.osr.gui.Controller.Visitor;
import de.fau.osr.gui.View.Presenter.Presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This Class is a Container for all information related to one commit.
 * @author: Florian Gerdes
 */
public class Commit extends DataElement {
    public List<String> requirements;
    public String id;
    public String message;
    public List<CommitFile> files;

    public Commit(String id, String message, List<String> requirements, List<CommitFile> files) {
        this.id = id;
        this.message = message;
        this.requirements = requirements;
        this.files = files;
    }

    public Commit(de.fau.osr.core.domain.Commit commit) {
        this.id = commit.getId();
        this.message = commit.getMessage();
        this.requirements.addAll(commit.getRequirements().stream().map(de.fau.osr.core.domain.Requirement::getId).collect(Collectors.toList()));
        files = new ArrayList<>();
        for (de.fau.osr.core.vcs.base.CommitFile commitFile : commit.getCommitFiles()) {
            files.add(new CommitFile(commitFile));
        }
    }

    @Override
    public Presenter visit(Visitor visitor) {
        return visitor.toPresenter(this);
    }

}
