package de.fau.osr.gui.View.ElementHandler;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import de.fau.osr.gui.View.Presenter.Presenter;

public class Impact_ElementHandler extends ElementHandler {
    private JLabel Requirements2Lines_label = new JLabel("#");
    
    
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
    public void setScrollPane_Content(Presenter[] presenter){
        super.setScrollPane_Content(presenter);
        list.setFixedCellHeight(12);
    }
}
