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
