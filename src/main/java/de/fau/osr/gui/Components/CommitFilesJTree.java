package de.fau.osr.gui.Components;

import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.gui.util.UiTools;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * <tt>JTree</tt> with ability to be built by array of commit files
 * Created by Dmitry Gorelenkov on 27.05.2015.
 */
public class CommitFilesJTree extends JTree {

    public CommitFilesJTree(CommitFile[] filesFromRequirement) {
        super(CommitFilesToTree(filesFromRequirement));
    }

    /**
     * converts array of <tt>CommitFile</tt>'s to Tree.
     * @param filesFromCommit array of CommitFiles
     * @return <tt>TreeNode</tt>, root of the generated tree
     */
    public static TreeNode CommitFilesToTree(CommitFile[] filesFromCommit) {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        for (CommitFile commitFile : filesFromCommit){
            UiTools.AddToTreeByPath(root, commitFile.newPath.toPath(), commitFile);
        }
        return root;
    }
}
