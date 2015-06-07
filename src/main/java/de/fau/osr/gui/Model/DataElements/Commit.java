package de.fau.osr.gui.Model.DataElements;

import java.util.List;

import de.fau.osr.gui.Controller.Visitor;
import de.fau.osr.gui.View.Presenter.Presenter;

/**
 * This Class is a Container within the test framework for all information related to one commit.
 * @author: Florian Gerdes
 */
public class Commit extends DataElement{
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
    
    @Override
    public Presenter visit(Visitor visitor) {
        return visitor.toPresenter(this);
    }
}
