package de.fau.osr.gui.View.ElementHandler;

import de.fau.osr.gui.Components.MultiSplitPane;
import de.fau.osr.gui.Model.DataElements.Requirement;
import de.fau.osr.gui.View.Presenter.Presenter_Requirement;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseMotionAdapter;
import java.util.function.Consumer;

public class Requirement_ElementHandler extends ElementHandler {
    
    
    private JLabel RequirementID_label = new JLabel("RequirementID");
    private JTextField RequirementID_textField = new JTextField();
    private JTextField RequirementSearch_textField = new JTextField();
    
    
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

    public void linkSize(GroupLayout layout){
        // make the requirement column non-resizable and have all elements with
        // the same horizontal size
        layout.linkSize(SwingConstants.HORIZONTAL, button,
                scrollPane, RequirementID_textField,
                RequirementSearch_textField);
    }

    public JTextField getTextField(){
        return RequirementID_textField;
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
        RequirementID_textField.setText("");
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
                    Requirement req = ((Presenter_Requirement) m.getElementAt(index)).getRequirement();
                    String tooltip = (req != null) ? req.getTitle() + "<br>" + req.getDescription() : "";
                    l.setToolTipText("<html><p>" + tooltip + "</p></html>");
                }
            }
        });
    }
    
}
