package de.fau.osr.gui.View.Renderer;

import de.fau.osr.gui.View.Presenter.Presenter;

import javax.swing.*;
import java.awt.*;

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
