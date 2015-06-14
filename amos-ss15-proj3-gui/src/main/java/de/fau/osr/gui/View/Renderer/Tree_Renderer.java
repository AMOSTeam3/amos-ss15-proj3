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

        //if a CommitFile
        if (content instanceof Presenter) {
            element = ((Presenter)content).present(element);
        }

        return element;
    }
}
