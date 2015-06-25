package de.fau.osr.gui.View.Presenter;

import de.fau.osr.gui.Controller.Visitor;
import de.fau.osr.gui.Model.DataElements.CommitFile;
import de.fau.osr.gui.Model.DataElements.DataElement;

import javax.swing.*;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;


public class Presenter_CommitFile extends Presenter{
    private ArrayList<CommitFile> commitFiles;

    public ArrayList<CommitFile> getCommitFile() {
        return commitFiles;
    }

    public void setCommitFile(ArrayList<CommitFile> commitFiles) {
        this.commitFiles = commitFiles;
    }
    
    public Presenter_CommitFile(ArrayList<CommitFile> commitFiles) {
        this.commitFiles = commitFiles;
    }

    public String getText(){
        float impact = 0;
        for(CommitFile commitFile: commitFiles){
            if(commitFile.impact > impact){
                impact = commitFile.impact;
            }
        }
        return String.format("%s - %f", commitFiles.get(0).newPath.toPath().getFileName().toString(), impact);
    }
    
    public boolean isAvailable(){
        File f = new File(commitFiles.get(0).workingCopy, commitFiles.get(0).newPath.getPath());
        return f.exists();
    }
    
    public Color getColor(){
        switch (commitFiles.get(0).commitState) {
        case MODIFIED:
            return Color.YELLOW;
        case ADDED:
            return Color.GREEN;
        case DELETED:
            return Color.RED;
        default:
            return Color.WHITE;
        }
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
