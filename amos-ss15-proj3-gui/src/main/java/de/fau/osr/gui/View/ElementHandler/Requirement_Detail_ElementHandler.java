package de.fau.osr.gui.View.ElementHandler;

import de.fau.osr.gui.View.Presenter.Presenter;
import de.fau.osr.gui.View.Presenter.Presenter_Requirement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Requirement_Detail_ElementHandler extends ElementHandler{
    private JTextArea title = new JTextArea(1,30);
    private JTextArea description = new JTextArea(5, 30);
    private JButton btnSave = new JButton("Save");
    
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
        

        JPanel bottomPane = new JPanel();
        bottomPane.add(btnSave);
        bottomPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        btnSave.setAlignmentX(Component.RIGHT_ALIGNMENT);

        panel.add(title);
        panel.add(description);
        panel.add(bottomPane);
        return panel;
    }
    
    public void setScrollPane_Content(Presenter[] presenter){
        Presenter_Requirement single_presenter = (Presenter_Requirement)presenter[presenter.length-1];
        title.setText(single_presenter.getRequirement().getTitle());
        description.setText(single_presenter.getRequirement().getDescription());
    }

    public void addListenerOnSaveClick(ActionListener listener) {
        btnSave.addActionListener(listener);
    }


    public JTextArea getTitle() {
        return title;
    }

    public JTextArea getDescription() {
        return description;
    }

    public JButton getBtnSave() {
        return btnSave;
    }
}
