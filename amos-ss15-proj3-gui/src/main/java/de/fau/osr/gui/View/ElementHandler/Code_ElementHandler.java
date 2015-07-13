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
package de.fau.osr.gui.View.ElementHandler;

import de.fau.osr.gui.View.Presenter.Presenter;

import javax.swing.*;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

import java.awt.*;

public class Code_ElementHandler extends ElementHandler {
    private JLabel Code_label = new JLabel("Code");
    
    
    public Code_ElementHandler(JScrollPane Requirements2Lines_scrollPane) {
        scrollPane = new JScrollPane();
        
        // synchronize vertical scrolling of code and req2line
        JScrollBar codeVertiSrollbar = scrollPane.getVerticalScrollBar();
        JScrollBar req2lineVertiScrollbar = Requirements2Lines_scrollPane
                .getVerticalScrollBar();
        codeVertiSrollbar.setModel(req2lineVertiScrollbar.getModel());
    }

    @Override
    public Component toComponent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(Code_label);
        panel.add(scrollPane);
        return panel;
    }

    @Override
    public void setScrollPane_Content(Presenter[] presenter){
        super.setScrollPane_Content(presenter);
        list.setFixedCellHeight(12);
    }
    
    @Override
    public double getWeight() {
		return 8;
	}
}
