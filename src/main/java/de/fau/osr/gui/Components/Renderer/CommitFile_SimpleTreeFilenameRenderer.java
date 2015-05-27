package de.fau.osr.gui.Components.Renderer;

import de.fau.osr.core.vcs.base.CommitFile;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * Display only the filename of CommitFile in the files tree
 */
public class CommitFile_SimpleTreeFilenameRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean sel,
                                                  boolean expanded,
                                                  boolean leaf, int row,
                                                  boolean hasFocus) {
        //DefaultTreeCellRenderer.getTreeCellRendererComponent() returns itself

        DefaultTreeCellRenderer element =
                (DefaultTreeCellRenderer) new DefaultTreeCellRenderer().getTreeCellRendererComponent(
                        tree, value, selected, expanded, leaf, row, hasFocus
                );

        //content of a node, could be a Path (if just directory) or a CommitFile
        Object content = ((DefaultMutableTreeNode) value).getUserObject();

        //if path, default .toString() method is called

        //if a CommitFile
        if (content instanceof CommitFile) {
            changeView(element, (CommitFile) content);
        }

        return element;
    }

    /**
     * modify the tree element by CommitFile content
     *
     * @param element    tree element, that will be prepared for rendering
     * @param commitFile commit file, that is content of the tree node
     */
    protected void changeView(DefaultTreeCellRenderer element, CommitFile commitFile) {
        element.setText(commitFile.newPath.toPath().getFileName().toString());
    }
}
