/*
 * This file is part of Req-Tracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * Req-Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Req-Tracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Req-Tracker.  If not, see <http://www.gnu.org/licenses/>.
 */
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
        return String.format("%s - %.1f", commitFiles.get(0).newPath.toPath().getFileName().toString(), impact);
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
