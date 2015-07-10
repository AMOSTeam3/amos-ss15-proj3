/*
 * This file is part of ReqTracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * ReqTracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ReqTracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with ReqTracker.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fau.osr.gui.View.Renderer;

import de.fau.osr.gui.View.Presenter.Presenter;

import javax.swing.*;
import java.awt.*;

public class List_Renderer implements
        ListCellRenderer<Presenter> {
    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
    
    @Override
    public Component getListCellRendererComponent(
            JList<? extends Presenter> list, Presenter value, int index,
            boolean isSelected, boolean cellHasFocus) {
        JLabel element = (JLabel) defaultRenderer.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);

        return value.present(element);
    }

}
