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
    
    private JTextField RequirementID_textField;
    private JTextField Commit_textField;
    private Presenter_Requirement requirement = null;
    private Presenter_Commit commit = null;
    
    public Linkage_ElementHandler(JTextField RequirementID_textField, JTextField Commit_textField){
        this.RequirementID_textField = RequirementID_textField;
        this.Commit_textField = Commit_textField;
        for(JTextField textField : new JTextField[]{this.RequirementID_textField, this.Commit_textField}) {
            textField.setEditable(false);
            textField.setColumns(10);
        }
        button = new JButton("Add Linkage");
    }

    @Override
    public Component toComponent() {
        return new MultiSplitPane(JSplitPane.HORIZONTAL_SPLIT, false)
                .addComponent(RequirementID_textField)
                .addComponent(Commit_textField)
                .addComponent(button);
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
