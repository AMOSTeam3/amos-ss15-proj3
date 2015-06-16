package de.fau.osr.gui.View;

import de.fau.osr.core.vcs.base.CommitFile;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.io.File;

public class CommitFile_SelectionListener implements TreeSelectionListener {
    private DefaultMutableTreeNode selectedBefore = null;
    private JTree commitFilesJTree;
    private Runnable action;

    public CommitFile_SelectionListener(JTree commitFilesJTree, Runnable action) {
        super();
        this.commitFilesJTree = commitFilesJTree;
        this.action = action;
    }
    
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        CommitFile commitFile;

        if (commitFilesJTree.getLastSelectedPathComponent() != null) {
            DefaultMutableTreeNode element = (DefaultMutableTreeNode) commitFilesJTree
                    .getLastSelectedPathComponent();
            if (element.getUserObject() instanceof CommitFile) {
                commitFile = (CommitFile) element.getUserObject();
                File f = new File(commitFile.newPath.getPath());
                if (f.exists()) {
                    selectedBefore = element;

                    commitFile = (CommitFile) selectedBefore.getUserObject();
                    
                    action.run();
                    
                    return;
                }
            }

            commitFilesJTree
                    .removeSelectionPath(new TreePath(element.getPath()));
        }

        if (selectedBefore != null) {
            commitFilesJTree.setSelectionPath(new TreePath(selectedBefore
                    .getPath()));
        }
    }
}
