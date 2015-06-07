package de.fau.osr.gui.View.Presenter;

import javax.swing.JLabel;

import de.fau.osr.gui.Controller.Visitor;
import de.fau.osr.gui.Model.DataElements.DataElement;

public abstract class Presenter {
    public abstract JLabel present(JLabel defaultLabel);
    
    public abstract DataElement visit(Visitor visitor);

    public abstract String getText();
}
