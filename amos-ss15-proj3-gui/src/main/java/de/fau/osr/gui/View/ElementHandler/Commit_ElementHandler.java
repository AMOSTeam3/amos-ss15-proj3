package de.fau.osr.gui.View.ElementHandler;

import de.fau.osr.gui.Components.MultiSplitPane;

import javax.swing.*;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import java.awt.*;

public class Commit_ElementHandler extends ElementHandler {
    
    private JTextField Commit_textField = new JTextField();
    private JLabel Commit_label = new JLabel("Commit");
    
    public Commit_ElementHandler(){
        scrollPane = new JScrollPane();
        button = new JButton("Navigate From Commit");
    }
    
    public JTextField getTextField(){
        return Commit_textField;
    }
    
    public ParallelGroup toHorizontalGroup(GroupLayout layout){
        return layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(Commit_textField)
//                .addComponent(button)
                .addComponent(Commit_label)
                .addComponent(scrollPane, 10, 100, Short.MAX_VALUE);
    }

    @Override
    public SequentialGroup toVerticalGroup(GroupLayout layout) {
        return layout.createSequentialGroup()
//                .addComponent(button)
                .addComponent(Commit_label)
                .addComponent(scrollPane);
    }

    @Override
    public Component toComponent() {
        return new MultiSplitPane(JSplitPane.VERTICAL_SPLIT, false)
//                .addComponent(button)
                .addComponent(Commit_label)
                .addComponent(scrollPane);
    }

    public void clear(){
        super.clear();
        Commit_textField.setText("");
    }
    
    
}
