package de.fau.osr.gui.View.Presenter;

import de.fau.osr.gui.Controller.Visitor;
import de.fau.osr.gui.Model.DataElements.AnnotatedLine;
import de.fau.osr.gui.Model.DataElements.DataElement;
import java.util.Collection;

import javax.swing.*;
import java.awt.*;


public class Presenter_Impact extends Presenter{
    private AnnotatedLine line;
    
    public AnnotatedLine getLine() {
        return line;
    }

    public void setLine(AnnotatedLine line) {
        this.line = line;
    }
    
    public Presenter_Impact(AnnotatedLine line){
        setLine(line);
    }
    
    public String getText(){
        String strline = "";
        for(String requirement: line.getRequirements()){
            strline = strline + " " + requirement;
        }
        return strline;
    }
    
    @Override
    public JLabel present(JLabel defaultLabel) {
        defaultLabel.setText(getText());
        return defaultLabel;
    }
    
    @Override
    public Collection<? extends DataElement> visit(Visitor visitor){
        return visitor.toDataElement(this);
    }

}
