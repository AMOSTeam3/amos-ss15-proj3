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
    public Collection<? extends DataElement> visit(Visitor visitor){
        return visitor.toDataElement(this);
    }
}
