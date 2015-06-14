package de.fau.osr.gui.View.Renderer;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class RowHeaderRenderer<E> extends JLabel implements ListCellRenderer<E> {

    public RowHeaderRenderer(JTable table) {
        JTableHeader header = table.getTableHeader();
        setOpaque(true);
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        setHorizontalAlignment(RIGHT);
        setForeground(header.getForeground());
        setBackground(header.getBackground());
        setFont(header.getFont());

        header.setResizingAllowed(true);
    }

    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        setText((value == null) ? "" : value.toString());
        return this;
    }
}

