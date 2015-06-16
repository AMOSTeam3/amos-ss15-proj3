package de.fau.osr.gui.View.ElementHandler;

import de.fau.osr.gui.Components.MultiSplitPane;
import de.fau.osr.gui.Controller.Visitor;
import de.fau.osr.gui.Model.DataElements.DataElement;
import de.fau.osr.gui.View.Presenter.Presenter;
import de.fau.osr.gui.View.Renderer.List_Renderer;

import javax.swing.*;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.util.ArrayList;
import java.util.Collection;

public abstract class ElementHandler {
    
    JButton button;
    JScrollPane scrollPane;
    JList<Presenter> list;
    
    
    public JButton getButton() {
        return button;
    }
    
    public JScrollPane getScrollPane() {
        return scrollPane;
    }
    
    public void clear(){
        JPanel panel = new JPanel(new GridLayout());
        panel.setBackground(Color.WHITE);
        scrollPane.setViewportView(panel);
    }

    public void setButtonAction(Runnable buttonAction) {
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                buttonAction.run();
            }
        });
    }
    
    public void setScrollPane_Content(Presenter[] presenter, Runnable action){
        list = new JList<Presenter>(presenter);
        ListCellRenderer<Presenter> renderer = new List_Renderer();
        list.setCellRenderer(renderer);
        
        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                action.run();
            }
        });
        
        JPanel panel = new JPanel(new GridLayout());
        panel.add(list);
        scrollPane.setViewportView(panel);
    }
    
    public Collection<DataElement> getSelection(Visitor visitor){
        ArrayList<DataElement> dataElements = new ArrayList<DataElement>();
        for(Presenter presenter: list.getSelectedValuesList()){
            dataElements.add(presenter.visit(visitor));
        }
        return dataElements;
    }
    
    public abstract ParallelGroup toHorizontalGroup(GroupLayout layout);
    
    public abstract SequentialGroup toVerticalGroup(GroupLayout layout);

    /**
     * Note: Returned Component will be added as columns to a MultiSplitePane.
     * @return  a vertically alignmend MultiSplitPane with elements "button" and "scrollpane"
     */
    public Component toComponent() {
        return new MultiSplitPane(JSplitPane.VERTICAL_SPLIT, false)
                .addComponent(button)
                .addComponent(scrollPane);
    }
}
