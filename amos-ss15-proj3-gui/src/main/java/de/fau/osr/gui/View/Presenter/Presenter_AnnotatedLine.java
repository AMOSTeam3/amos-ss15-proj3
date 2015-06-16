package de.fau.osr.gui.View.Presenter;

import de.fau.osr.gui.Controller.Visitor;
import de.fau.osr.gui.Model.DataElements.AnnotatedLine;
import de.fau.osr.gui.Model.DataElements.DataElement;
import de.fau.osr.gui.Model.DataElements.Requirement;
import java.util.Collection;

import javax.swing.*;
import java.awt.*;

public class Presenter_AnnotatedLine extends Presenter{
    private AnnotatedLine line;
    private Collection<Requirement> RequirementID;

    public AnnotatedLine getLine() {
        return line;
    }

    public void setLine(AnnotatedLine line) {
        this.line = line;
    }
    
    public void setRequirementID(Collection<Requirement> RequirementID){
        this.RequirementID = RequirementID;
    }
    
    public String getText(){
        return line.getLine();
    }
    
    public Presenter_AnnotatedLine(AnnotatedLine line){
        setLine(line);
    }
    
    public boolean isHighlighted(){
        for(Requirement requirement: RequirementID){
            if(line.getRequirements().contains(requirement.getID())){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString(){
        return line.getLine();
    }

    @Override
    public JLabel present(JLabel defaultLabel) {
        if (isHighlighted()) {
            defaultLabel.setForeground(Color.RED);
        }
        defaultLabel.setText(getText());
        return defaultLabel;
    }
    
    @Override
    public DataElement visit(Visitor visitor){
        return visitor.toDataElement(this);
    }
}
