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
package de.fau.osr.gui.Components;

import de.fau.osr.gui.util.UiTools;

import javax.swing.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import de.fau.osr.gui.View.Presenter.Presenter_Path;
import de.fau.osr.gui.View.Presenter.Presenter_PathImpact;
import de.fau.osr.gui.View.Presenter.Presenter;

/**
 * <tt>JTree</tt> with ability to be built by array of commit files
 * Created by Dmitry Gorelenkov on 27.05.2015.
 */
public class PathDEsJTree extends JTree {

    public PathDEsJTree(Presenter[] filesFromRequirement) {
        super(PathDEsToTree(filesFromRequirement));
        this.setSelectionModel(new PathDEsJTreeSelectionModel());
        this.getSelectionModel().setSelectionMode
        (TreeSelectionModel.SINGLE_TREE_SELECTION);
    }
    
    public void addSelectionListener(TreeSelectionListener listener){
        this.addTreeSelectionListener(listener);
    }

    /**
     * converts array of <tt>PathDE</tt>'s to Tree.
     * @param filesFromCommit array of PathDEs
     * @return <tt>TreeNode</tt>, root of the generated tree
     */
    public static TreeNode PathDEsToTree(Presenter[] filesFromCommit) {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        for (Presenter pathDE : filesFromCommit){
            if(pathDE instanceof Presenter_Path){
                UiTools.AddToTreeByPath(root, ((Presenter_Path)pathDE).getPathDE().get(0).FilePath, pathDE);                
            }else{
                UiTools.AddToTreeByPath(root, ((Presenter_PathImpact)pathDE).getPathDE().get(0).FilePath, pathDE);
            }
        }
        return root;
    }
}
