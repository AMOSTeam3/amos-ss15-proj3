package de.fau.osr.gui.View.Presenter;

import de.fau.osr.gui.Controller.Visitor;
import de.fau.osr.gui.Model.DataElements.DataElement;
import de.fau.osr.gui.Model.DataElements.PathDE;

import javax.swing.*;

import java.awt.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Presenter for DataElement PathDE
 * @author: Taleh Didover
 */
public class Presenter_Path extends Presenter{
    private ArrayList<PathDE> filePaths;

    public ArrayList<PathDE> getPathDE() {
        return filePaths;
    }

    public void setPathDE(ArrayList<PathDE> commitFiles) {
        this.filePaths = commitFiles;
    }

    public Presenter_Path(ArrayList<PathDE> filePaths) {
        this.filePaths = filePaths;
    }

    public String getText(){
        String pathDE = filePaths.get(0).FilePath.getFileName().toString();
        return pathDE;
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
