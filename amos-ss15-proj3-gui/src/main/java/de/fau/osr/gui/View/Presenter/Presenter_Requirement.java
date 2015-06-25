package de.fau.osr.gui.View.Presenter;

import java.util.Collection;

import de.fau.osr.gui.Controller.Visitor;
import de.fau.osr.gui.Model.DataElements.DataElement;
import de.fau.osr.gui.Model.DataElements.Requirement;

import javax.swing.*;

public class Presenter_Requirement extends Presenter{
    private Requirement Requirement;

    public Requirement getRequirement(){
        return Requirement;
    }
    
    public Presenter_Requirement(Requirement dataElement) {
        this.Requirement = dataElement;
    }
    
    @Override
    public JLabel present(JLabel defaultLabel) {
        defaultLabel.setText(getText());
        return defaultLabel;
    }

    @Override
    public String getText() {
        return Requirement.getID();
    }
    
    @Override
    public Collection<? extends DataElement> visit(Visitor visitor){
        return visitor.toDataElement(this);
    }
}
