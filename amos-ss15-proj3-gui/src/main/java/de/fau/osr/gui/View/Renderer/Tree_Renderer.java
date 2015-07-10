/*
 * This file is part of ReqTracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * ReqTracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ReqTracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with ReqTracker.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fau.osr.gui.View.Renderer;

import de.fau.osr.gui.View.Presenter.Presenter;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;


/**
 * Display only the filename of CommitFile in the files tree
 */
public class Tree_Renderer extends DefaultTreeCellRenderer {
    private static final long serialVersionUID = 1L;

    public  Tree_Renderer() {
        setOpaque(true);
    }
    
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean sel,
                                                  boolean expanded,
                                                  boolean leaf, int row,
                                                  boolean hasFocus) {
        //DefaultTreeCellRenderer.getTreeCellRendererComponent() returns itself
        JLabel element = (DefaultTreeCellRenderer) new DefaultTreeCellRenderer().getTreeCellRendererComponent(
                        tree, value, selected, expanded, leaf, row, hasFocus);

        //content of a node, could be a Path (if just directory) or a CommitFile
        Object content = ((DefaultMutableTreeNode) value).getUserObject();

        //if path, default .toString() method is called
        element.setOpaque(true);

        //if a CommitFile
        if (content instanceof Presenter) {
            element = ((Presenter)content).present(element);
        }

        return element;
    }
}
