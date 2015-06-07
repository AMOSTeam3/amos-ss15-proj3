package de.fau.osr.gui.View.Renderer;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import de.fau.osr.gui.View.Presenter.Presenter;

public class List_Renderer implements
        ListCellRenderer<Presenter> {
    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
    
    @Override
    public Component getListCellRendererComponent(
            JList<? extends Presenter> list, Presenter value, int index,
            boolean isSelected, boolean cellHasFocus) {
        JLabel element = (JLabel) defaultRenderer.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);

        return value.present(element);
    }

}
