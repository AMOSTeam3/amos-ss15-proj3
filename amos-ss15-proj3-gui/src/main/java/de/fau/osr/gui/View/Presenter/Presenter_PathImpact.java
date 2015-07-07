package de.fau.osr.gui.View.Presenter;

import de.fau.osr.gui.Controller.Visitor;
import de.fau.osr.gui.Model.DataElements.DataElement;
import de.fau.osr.gui.Model.DataElements.PathDE;
import de.fau.osr.gui.Model.DataElements.ImpactDE;

import javax.swing.*;

import java.awt.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Presenter for DataElement PathDE
 * @author: Taleh Didover
 */
public class Presenter_PathImpact extends Presenter{
    private ArrayList<PathDE> filePaths;
    private ImpactDE impact;

    public ArrayList<PathDE> getPathDE() {
        return filePaths;
    }
    
    public ImpactDE getImpact(){
        return impact;
    }

    public void setPathDE(ArrayList<PathDE> commitFiles) {
        this.filePaths = commitFiles;
    }
    
    public void setImpact(ImpactDE impact){
        this.impact = impact;
    }

    public Presenter_PathImpact(ArrayList<PathDE> filePaths, ImpactDE impact) {
        this.filePaths = filePaths;
        this.impact = impact;
    }

    public String getText(){
        String pathDE = filePaths.get(0).FilePath.getFileName().toString();
        return String.format("%s - %.1f", pathDE, impact.Impact);
    }

    public boolean isAvailable(){
        PathDE pathDE = filePaths.get(0);
        return Files.exists(pathDE.FilePath);
    }

    public Color getColor(){
        return Color.WHITE;
    }

    @Override
    public JLabel present(JLabel defaultLabel) {
        defaultLabel.setBackground(getColor());
        defaultLabel.setText(getText());

        if(!isAvailable()){
            defaultLabel.setForeground(UIManager
                    .getColor("Label.disabledForeground"));
            defaultLabel.setBorder(null);
        }
        return defaultLabel;
    }

    @Override
    public Collection<? extends DataElement> visit(Visitor visitor){
        return visitor.toDataElement(this);
    }
}
