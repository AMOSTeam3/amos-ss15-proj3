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

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;

public class PathDEsJTreeSelectionModel extends DefaultTreeSelectionModel {
    private static final long serialVersionUID = 1L;

    private TreePath[] augmentPaths(TreePath[] pPaths) {
        ArrayList<TreePath> paths = new ArrayList<TreePath>();

        for (int i = 0; i < pPaths.length; i++) {
            if (((DefaultMutableTreeNode) pPaths[i].getLastPathComponent())
                    .isLeaf()) {
                paths.add(pPaths[i]);
            }
        }

        return paths.toArray(pPaths);
    }

    @Override
    public void setSelectionPaths(TreePath[] pPaths) {
        super.setSelectionPaths(augmentPaths(pPaths));
    }

    @Override
    public void addSelectionPaths(TreePath[] pPaths) {
        super.addSelectionPaths(augmentPaths(pPaths));
    }
}