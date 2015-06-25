package de.fau.osr.gui.View.ElementHandler;

import de.fau.osr.core.db.dao.impl.RequirementDaoImplementation;
import de.fau.osr.core.db.domain.Requirement;
import de.fau.osr.gui.Components.MultiSplitPane;
import de.fau.osr.gui.View.Presenter.Presenter;

import javax.swing.*;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseMotionAdapter;
import java.util.function.Consumer;

public class Requirement_ElementHandler extends ElementHandler {
    
    
    protected JLabel RequirementID_label = new JLabel("RequirementID");
    protected JTextField RequirementSearch_textField = new JTextField();
    
    
    public Requirement_ElementHandler(){
        button = new JButton("Navigate From ID");
        scrollPane = new JScrollPane();
    }

    @Override
    public Component toComponent() {
        return new MultiSplitPane(JSplitPane.VERTICAL_SPLIT, false)
                .addComponent(button)
                .addComponent(RequirementID_label)
                .addComponent(RequirementSearch_textField)
                .addComponent(scrollPane);
    }
    
    public void setSearchTextFieldAction(Consumer<JTextField> action) {
        RequirementSearch_textField.getDocument().addDocumentListener(
            new DocumentListener() {

                @Override
                public void removeUpdate(DocumentEvent e) {
                    action.accept(RequirementSearch_textField);
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    action.accept(RequirementSearch_textField);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    action.accept(RequirementSearch_textField);
                }
            }
        );
    }
    
    public void clear(){
        super.clear();
    }
    
    
    public void setOnClickAction(Runnable action){
        super.setOnClickAction(action);
        
        list.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                JList l = (JList)e.getSource();
                ListModel m = l.getModel();
                int index = l.locationToIndex(e.getPoint());
                if (index > -1) {
                    String id = m.getElementAt(index).toString();
                    Requirement req = new RequirementDaoImplementation().getRequirementById(id);
                    String tooltip = (req != null) ? req.getTitle() + "<br>" + req.getDescription() : "";
                    l.setToolTipText("<html><p>" + tooltip + "</p></html>");
                }
            }
        });
    }
    
}
