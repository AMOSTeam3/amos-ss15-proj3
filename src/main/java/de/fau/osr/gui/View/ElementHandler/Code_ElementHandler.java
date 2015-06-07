package de.fau.osr.gui.View.ElementHandler;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import de.fau.osr.gui.Controller.Visitor;
import de.fau.osr.gui.Model.DataElements.AnnotatedLine;
import de.fau.osr.gui.Model.DataElements.DataElement;
import de.fau.osr.gui.View.Presenter.Presenter;

public class Code_ElementHandler extends ElementHandler {
    private JLabel Code_label = new JLabel("Code");
    
    
    public Code_ElementHandler(JScrollPane Requirements2Lines_scrollPane) {
        scrollPane = new JScrollPane();
        
        // synchronize vertical scrolling of code and req2line
        JScrollBar codeVertiSrollbar = scrollPane.getVerticalScrollBar();
        JScrollBar req2lineVertiScrollbar = Requirements2Lines_scrollPane
                .getVerticalScrollBar();
        codeVertiSrollbar.setModel(req2lineVertiScrollbar.getModel());
    }
    
    @Override
    public ParallelGroup toHorizontalGroup(GroupLayout layout) {
        return layout.createParallelGroup(GroupLayout.Alignment.CENTER)
        .addComponent(Code_label)
        .addComponent(scrollPane, 10, 400, Short.MAX_VALUE);
    }
    @Override
    public SequentialGroup toVerticalGroup(GroupLayout layout) {
        return layout.createSequentialGroup()
                .addComponent(Code_label)
                .addComponent(scrollPane);
    }
    
    @Override
    public void setScrollPane_Content(Presenter[] presenter){
        super.setScrollPane_Content(presenter);
        list.setFixedCellHeight(12);
    }
}
