package de.fau.osr.gui.Components;

import de.fau.osr.gui.util.UiTools;

import javax.swing.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;
import de.fau.osr.gui.View.Presenter.Presenter_CommitFile;
import de.fau.osr.gui.View.Presenter.Presenter;

/**
 * <tt>JTree</tt> with ability to be built by array of commit files
 * Created by Dmitry Gorelenkov on 27.05.2015.
 */
public class CommitFilesJTree extends JTree {

    public CommitFilesJTree(Presenter[] filesFromRequirement) {
        super(CommitFilesToTree(filesFromRequirement));
        this.getSelectionModel().setSelectionMode
        (TreeSelectionModel.SINGLE_TREE_SELECTION);
    }
    
    public void addSelectionListener(TreeSelectionListener listener){
        this.addTreeSelectionListener(listener);
    }

    /**
     * converts array of <tt>CommitFile</tt>'s to Tree.
     * @param filesFromCommit array of CommitFiles
     * @return <tt>TreeNode</tt>, root of the generated tree
     */
    public static TreeNode CommitFilesToTree(Presenter[] filesFromCommit) {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        for (Presenter commitFile : filesFromCommit){
            UiTools.AddToTreeByPath(root, ((Presenter_CommitFile)commitFile).getCommitFile().newPath.toPath(), commitFile);
        }
        return root;
    }
}
