package de.fau.osr.gui.Model.DataElements;

import de.fau.osr.gui.Controller.Visitor;
import de.fau.osr.gui.View.Presenter.Presenter;



public class Requirement extends DataElement {
    private String id;

    public String getID() {
        return id;
    }

    @Override
    public Presenter visit(Visitor visitor) {
        return visitor.toPresenter(this);
    }

}
