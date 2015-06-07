package de.fau.osr.gui.View.ElementHandler;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import de.fau.osr.gui.Controller.Visitor;
import de.fau.osr.gui.Model.DataElements.DataElement;
import de.fau.osr.gui.View.Presenter.Presenter;
import de.fau.osr.gui.View.Renderer.List_Renderer;

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
    
    public void setScrollPane_Content(Presenter[] presenter){
        list = new JList<Presenter>(presenter);
        ListCellRenderer<Presenter> renderer = new List_Renderer();
        list.setCellRenderer(renderer);
        
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
}
