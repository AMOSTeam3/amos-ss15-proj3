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
    private lambda lambda;
    
    interface lambda{
        void doByTrigger();
    }

    public CommitFile_SelectionListener(JTree commitFilesJTree, lambda lambda) {
        super();
        this.commitFilesJTree = commitFilesJTree;
        this.lambda = lambda;
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
                    
                    lambda.doByTrigger();
                    
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
