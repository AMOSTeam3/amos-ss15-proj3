package de.fau.osr.gui.View.Presenter;

import de.fau.osr.gui.Controller.Visitor;
import de.fau.osr.gui.Model.DataElements.DataElement;

import javax.swing.*;

public abstract class Presenter {
    public abstract JLabel present(JLabel defaultLabel);
    
    public abstract DataElement visit(Visitor visitor);

    public abstract String getText();
}
