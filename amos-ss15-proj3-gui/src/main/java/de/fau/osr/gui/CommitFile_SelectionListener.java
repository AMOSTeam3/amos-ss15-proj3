package de.fau.osr.gui;

import java.io.File;

import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import de.fau.osr.core.vcs.base.CommitFile;

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
                File f = new File(commitFile.workingCopy, commitFile.newPath.getPath());
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
