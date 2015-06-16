package de.fau.osr.gui.View.ElementHandler;

import de.fau.osr.gui.Components.MultiSplitPane;
import de.fau.osr.gui.View.Presenter.Presenter;

import javax.swing.*;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import java.awt.*;

public class Impact_ElementHandler extends ElementHandler {
    private JLabel Requirements2Lines_label = new JLabel("##");
    
    
    public Impact_ElementHandler() {
        scrollPane = new JScrollPane();
        
        // hide vertical scrollbar of req2line
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
    }
    
    @Override
    public ParallelGroup toHorizontalGroup(GroupLayout layout) {
        return layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(Requirements2Lines_label)
                .addComponent(scrollPane, 10, 30, 30);
    }

    @Override
    public SequentialGroup toVerticalGroup(GroupLayout layout) {
        return layout.createSequentialGroup()
                .addComponent(Requirements2Lines_label)
                .addComponent(scrollPane);
    }

    @Override
    public Component toComponent() {
        return new MultiSplitPane(JSplitPane.VERTICAL_SPLIT, false)
            .addComponent(Requirements2Lines_label)
            .addComponent(scrollPane);
    }

    @Override
    public void setScrollPane_Content(Presenter[] presenter){
        super.setScrollPane_Content(presenter);
        list.setFixedCellHeight(12);
    }
}
