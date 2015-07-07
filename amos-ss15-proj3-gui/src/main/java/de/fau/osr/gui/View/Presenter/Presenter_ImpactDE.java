package de.fau.osr.gui.View.Presenter;

import de.fau.osr.gui.Controller.Visitor;
import de.fau.osr.gui.Model.DataElements.AnnotatedLine;
import de.fau.osr.gui.Model.DataElements.DataElement;
import de.fau.osr.gui.Model.DataElements.ImpactDE;

import java.util.Collection;

import javax.swing.*;

import java.awt.*;


public class Presenter_ImpactDE extends Presenter{
    private ImpactDE impact;
    
    public ImpactDE getImpact() {
        return impact;
    }

    public void setImpact(ImpactDE impact) {
        this.impact = impact;
    }
    
    public Presenter_ImpactDE(ImpactDE impact){
        setImpact(impact);
    }
    
    public String getText(){
        return impact.toString();
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
