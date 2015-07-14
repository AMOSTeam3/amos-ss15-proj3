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

import de.fau.osr.gui.Model.DataElements.Commit;
import de.fau.osr.gui.Model.DataElements.Requirement;
import de.fau.osr.gui.View.Presenter.Presenter_Commit;
import de.fau.osr.gui.View.Presenter.Presenter_Requirement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Linkage_ElementHandler extends ElementHandler {

    private final JButton btnRemoveLinkage;

    public void setOnClickAddLinkage(ActionListener action) {
        for(ActionListener a : button.getActionListeners())
            button.removeActionListener(a);
        button.addActionListener(action);
    }

    public void setBtnRemoveLinkageAction(Runnable action) {
        for(ActionListener a : btnRemoveLinkage.getActionListeners())
            btnRemoveLinkage.removeActionListener(a);
        btnRemoveLinkage.addActionListener(e -> action.run());
    }

    public enum ButtonState{Deactivate, Activate}
    private ButtonState buttonState = ButtonState.Activate;
    
    private JTextField RequirementID_textField = new JTextField();
    private JTextField Commit_textField = new JTextField();
    private Presenter_Requirement requirement = null;
    private Presenter_Commit commit = null;
    private Boolean isDataLayerChanged = false;
    
    public Linkage_ElementHandler(){
        for(JTextField textField : new JTextField[]{this.RequirementID_textField, this.Commit_textField}) {
            textField.setEditable(false);
            textField.setColumns(50);
        }
        button = new JButton("Add Linkage");
        button.setPreferredSize(new Dimension(20, 5));
        btnRemoveLinkage = new JButton("Remove Linkage");
        btnRemoveLinkage.setPreferredSize(new Dimension(20, 5));
    }
    
    public Boolean isDataLayerChanged(){
        return isDataLayerChanged;
    }
    
    public void setDataLayerChanged(Boolean isChanged){
        isDataLayerChanged = isChanged;
    }

    @Override
    public Component toComponent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        panel.add(Box.createRigidArea(new Dimension(0, 50)));
        
        RequirementID_textField.setAlignmentX(Component.LEFT_ALIGNMENT);
        RequirementID_textField.setMaximumSize(new Dimension(500, 25));
        RequirementID_textField.setPreferredSize(new Dimension(500, 25));
        RequirementID_textField.setMinimumSize(new Dimension(500, 25));
        RequirementID_textField.setBackground(Color.WHITE);
        panel.add(RequirementID_textField);
        
        panel.add(Box.createRigidArea(new Dimension(0,10)));
        
        Commit_textField.setAlignmentX(Component.LEFT_ALIGNMENT);
        Commit_textField.setMaximumSize(new Dimension(500, 25));
        Commit_textField.setPreferredSize(new Dimension(500, 25));
        Commit_textField.setMinimumSize(new Dimension(500, 25));
        Commit_textField.setBackground(Color.WHITE);
        panel.add(Commit_textField);
        
        panel.add(Box.createRigidArea(new Dimension(0,10)));
        
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(button);
        panel.add(btnRemoveLinkage);

        return panel;
    }

    @Override
    public void setButtonAction(Runnable action) {
		setOnClickAddLinkage(e->action.run());
    }

    public void clear(){
        RequirementID_textField.setText("");
        Commit_textField.setText("");
        buttonState = ButtonState.Deactivate;
    }

    public void setRequirement(Presenter_Requirement requirement) {
        this.requirement = requirement;
        RequirementID_textField.setText(requirement.getText());
    }
    
    public void setCommit(Presenter_Commit commit) {
        this.commit = commit;
        Commit_textField.setText(commit.getText());
    }

    public Commit getCommit() {
        return commit.getCommit();
    }

    public Requirement getRequirement() {
        return requirement.getRequirement();
    }
    
    public void switchButtonAction(){
        switch(buttonState){
        case Activate:
            buttonState = ButtonState.Deactivate;
            break;
        case Deactivate:
            buttonState = ButtonState.Activate;
            break;
        }
    }
}
