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
