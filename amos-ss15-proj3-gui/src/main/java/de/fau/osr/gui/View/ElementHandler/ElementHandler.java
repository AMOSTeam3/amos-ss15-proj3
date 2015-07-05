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
    	for(ActionListener a : button.getActionListeners()) {
    		button.removeActionListener(a);
    	}
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                buttonAction.run();
            }
        });
    }
    
    public void setScrollPane_Content(Presenter[] presenter){
        list = new JList<Presenter>(presenter);
        ListCellRenderer<Presenter> renderer = new List_Renderer();
        list.setCellRenderer(renderer);
        
        JPanel panel = new JPanel(new GridLayout());
        panel.add(list);
        scrollPane.setViewportView(panel);
    }
    
    public void setOnClickAction(Runnable action){
        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if(listSelectionEvent.getValueIsAdjusting() == true)
                    return;
                action.run();
            }
        });
    }
    
    public Collection<DataElement> getSelection(Visitor visitor){
        ArrayList<DataElement> dataElements = new ArrayList<DataElement>();
        for(Presenter presenter: list.getSelectedValuesList()){
            dataElements.addAll(presenter.visit(visitor));
        }
        return dataElements;
    }

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
