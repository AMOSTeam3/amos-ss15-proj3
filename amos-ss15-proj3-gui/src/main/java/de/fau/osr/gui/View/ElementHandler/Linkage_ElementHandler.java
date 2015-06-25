package de.fau.osr.gui.View.ElementHandler;

import de.fau.osr.gui.Components.MultiSplitPane;
import de.fau.osr.gui.Model.DataElements.Commit;
import de.fau.osr.gui.Model.DataElements.Requirement;
import de.fau.osr.gui.View.Presenter.Presenter_Commit;
import de.fau.osr.gui.View.Presenter.Presenter_Requirement;

import javax.swing.*;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public class Linkage_ElementHandler extends ElementHandler {
    public enum ButtonState{Deactivate, Activate}
    private ButtonState buttonState = ButtonState.Activate;
    
    private JTextField RequirementID_textField = new JTextField();
    private JTextField Commit_textField = new JTextField();
    private Presenter_Requirement requirement = null;
    private Presenter_Commit commit = null;
    
    public Linkage_ElementHandler(){
        for(JTextField textField : new JTextField[]{this.RequirementID_textField, this.Commit_textField}) {
            textField.setEditable(false);
            textField.setColumns(50);
        }
        button = new JButton("Add Linkage");
        button.setPreferredSize(new Dimension(20, 5));
    }

    @Override
    public Component toComponent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        panel.add(Box.createRigidArea(new Dimension(0,50)));
        
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
        
        return panel;
    }

    public void setButtonAction(Consumer<ButtonState> action) {
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                action.accept(buttonState);
            }
        });
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
