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

import java.util.Collection;

import de.fau.osr.gui.Controller.Visitor;
import de.fau.osr.gui.Model.DataElements.DataElement;
import de.fau.osr.gui.Model.DataElements.Requirement;

import javax.swing.*;

public class Presenter_Requirement extends Presenter{
    private Requirement Requirement;

    public Requirement getRequirement(){
        return Requirement;
    }
    
    public Presenter_Requirement(Requirement dataElement) {
        this.Requirement = dataElement;
    }
    
    @Override
    public JLabel present(JLabel defaultLabel) {
        defaultLabel.setText(getText());
        return defaultLabel;
    }

    @Override
    public String getText() {
        return Requirement.getID();
    }
    
    @Override
    public Collection<? extends DataElement> visit(Visitor visitor){
        return visitor.toDataElement(this);
    }
}
