package de.fau.osr.gui.View.Presenter;

import de.fau.osr.gui.Controller.Visitor;
import de.fau.osr.gui.Model.DataElements.AnnotatedLine;
import de.fau.osr.gui.Model.DataElements.DataElement;

import javax.swing.*;
import java.awt.*;

public class Presenter_AnnotatedLine extends Presenter{
    private AnnotatedLine line;
    private String RequirementID;

    public AnnotatedLine getLine() {
        return line;
    }

    public void setLine(AnnotatedLine line) {
        this.line = line;
    }
    
    public void setRequirementID(String RequirementID){
        this.RequirementID = RequirementID;
    }
    
    public String getText(){
        return line.getLine();
    }
    
    public Presenter_AnnotatedLine(AnnotatedLine line){
        setLine(line);
    }
    
    public boolean isHighlighted(){
        return line.getRequirements().contains(RequirementID);
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
