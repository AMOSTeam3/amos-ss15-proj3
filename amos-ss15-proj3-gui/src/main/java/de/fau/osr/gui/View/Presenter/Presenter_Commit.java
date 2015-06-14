package de.fau.osr.gui.View.Presenter;


import de.fau.osr.gui.Controller.Visitor;
import de.fau.osr.gui.Model.DataElements.Commit;
import de.fau.osr.gui.Model.DataElements.DataElement;

import javax.swing.*;

public class Presenter_Commit extends Presenter{
    private Commit commit;

    public Commit getCommit(){
        return commit;
    }
    
    public Presenter_Commit(Commit commit) {
        this.commit = commit;
    }
    
    @Override
    public JLabel present(JLabel defaultLabel) {
        defaultLabel.setText(getText());
        return defaultLabel;
    }

    @Override
    public String getText() {
        return commit.message;
    }
    
    @Override
    public DataElement visit(Visitor visitor){
        return visitor.toDataElement(this);
    }
}
