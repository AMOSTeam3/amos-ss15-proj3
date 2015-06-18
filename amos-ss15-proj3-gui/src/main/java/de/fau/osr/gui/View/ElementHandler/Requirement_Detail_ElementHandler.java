package de.fau.osr.gui.View.ElementHandler;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.geom.Dimension2D;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;

import de.fau.osr.gui.View.Presenter.Presenter;
import de.fau.osr.gui.View.Presenter.Presenter_Requirement;
import de.fau.osr.gui.View.Renderer.List_Renderer;

public class Requirement_Detail_ElementHandler extends ElementHandler{
    private JTextArea title = new JTextArea(1,30);
    private JTextArea description = new JTextArea(5, 30);
    
    public Requirement_Detail_ElementHandler(){
        title.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        description.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    }

    public Component toComponent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        
//        title.setPreferredSize(new Dimension(10, 5));
//        title.setAlignmentX(Component.LEFT_ALIGNMENT);
//        description.setPreferredSize(new Dimension(10, 5));
//        description.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(title);
        panel.add(description);
        return panel;
    }
    
    public void setScrollPane_Content(Presenter[] presenter){
        Presenter_Requirement single_presenter = (Presenter_Requirement)presenter[presenter.length-1];
        title.setText(single_presenter.getRequirement().getTitle());
        description.setText(single_presenter.getRequirement().getDescription());
    }
}
