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
package de.fau.osr.gui.View.ElementHandler;

import de.fau.osr.gui.View.Presenter.Presenter;
import de.fau.osr.gui.View.Presenter.Presenter_Requirement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Requirement_Detail_ElementHandler extends ElementHandler{
    private JTextArea title = new JTextArea(1,30);
    private JTextArea description = new JTextArea(5, 30);
    private JButton btnSave = new JButton("Save");
    
    public Requirement_Detail_ElementHandler(){
        title.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        description.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    }

    public Component toComponent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        
//        title.setPreferredSize(new Dimension(10, 5));
//        title.setAlignmentX(Component.LEFT_ALIGNMENT);
//        description.setPreferredSize(new Dimension(10, 5));
//        description.setAlignmentX(Component.LEFT_ALIGNMENT);
        

        JPanel bottomPane = new JPanel();
        bottomPane.add(btnSave);
        bottomPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        btnSave.setAlignmentX(Component.RIGHT_ALIGNMENT);

        panel.add(title);
        panel.add(description);
        panel.add(bottomPane);
        return panel;
    }
    
    public void setScrollPane_Content(Presenter[] presenter){
        Presenter_Requirement single_presenter = (Presenter_Requirement)presenter[presenter.length-1];
        title.setText(single_presenter.getRequirement().getTitle());
        description.setText(single_presenter.getRequirement().getDescription());
    }

    public void setListenerOnSaveClick(ActionListener listener) {
        for(ActionListener a : btnSave.getActionListeners())
            btnSave.removeActionListener(a);
        btnSave.addActionListener(listener);
    }


    public JTextArea getTitle() {
        return title;
    }

    public JTextArea getDescription() {
        return description;
    }

    public JButton getBtnSave() {
        return btnSave;
    }
}
