/*
 * This file is part of Req-Tracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * Req-Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Req-Tracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Req-Tracker.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fau.osr.gui.View.ElementHandler;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import de.fau.osr.gui.Components.MultiSplitPane;
import de.fau.osr.gui.Components.PathDEsJTree;
import de.fau.osr.gui.Components.PathDEsJTreeSelectionModel;
import de.fau.osr.gui.Controller.Visitor;
import de.fau.osr.gui.Controller.Visitor_Swing;
import de.fau.osr.gui.Model.DataElements.DataElement;
import de.fau.osr.gui.Model.DataElements.PathDE;
import de.fau.osr.gui.Model.DataElements.ImpactDE;
import de.fau.osr.gui.View.Presenter.Presenter;
import de.fau.osr.gui.View.Presenter.Presenter_Path;
import de.fau.osr.gui.View.Presenter.Presenter_PathImpact;
import de.fau.osr.gui.View.Renderer.Tree_Renderer;

public class PathDE_ElementHandler extends ElementHandler {
    
    private JLabel Files_label = new JLabel("Files");
    private PathDEsJTree tree;
    
    public PathDE_ElementHandler(){
        button = new JButton("Navigate From File");
        scrollPane = new JScrollPane();
    }

    @Override
    public Component toComponent() {
        return new MultiSplitPane(JSplitPane.VERTICAL_SPLIT, false)
                .addComponent(button)
                .addComponent(Files_label)
                .addComponent(scrollPane);
    }


    public void setScrollPane_Content(Presenter[] elements){
        
        elements = deleteMultiplikations(elements);
        
        if(elements.length == 0){
            return;
        }
        tree  = new PathDEsJTree(elements);
        
      //expand all nodes
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
        
        Tree_Renderer renderer = new Tree_Renderer();
        tree.setCellRenderer(renderer);
        
        JPanel panel = new JPanel(new GridLayout());
        panel.add(tree);
        scrollPane.setViewportView(panel);
    }
    
    /**
     * Collapses multiple PathDEs into one Presenter, if the newPath of the Files are the same.
     * @param presenters
     * @return
     */
    private Presenter[] deleteMultiplikations(Presenter[] presenters){
        ArrayList<Presenter> result = new ArrayList<Presenter>();
        String path = null;
        int type = -1; // 0 = Presenter_Path 1 = Presenter_PathImpact
        ArrayList<ArrayList<PathDE>> PathDEs = new ArrayList<ArrayList<PathDE>>();
        ArrayList<ImpactDE> ImpactDEs = new ArrayList<ImpactDE>();
        for(Presenter presenter: presenters){
            
            PathDE pathDE;
            if(presenter instanceof Presenter_Path){
                type = 0;
                pathDE = ((Presenter_Path) presenter).getPathDE().get(0);
            }else{
                type = 1;
                pathDE = ((Presenter_PathImpact) presenter).getPathDE().get(0);
            }
            
            boolean different = true;
            for(ArrayList<PathDE> bucket: PathDEs){
                if(pathDE.FilePath.equals(bucket.get(0).FilePath)){
                    bucket.add(pathDE);
                    different = false;
                    break;
                }
            }
            if(different){
                ArrayList<PathDE> newBucket = new ArrayList<PathDE>();
                newBucket.add(pathDE);
                PathDEs.add(newBucket);
                if(type == 1){
                    ImpactDEs.add(((Presenter_PathImpact) presenter).getImpact());
                }
            }
        }
        
        int i = 0;
        for(ArrayList<PathDE> bucket: PathDEs){
            if(type == 0){
                result.add(new Presenter_Path(bucket));                
            }else if(type == 1){
                result.add(new Presenter_PathImpact(bucket, ImpactDEs.get(i)));
            }else{
                break;
            }
            i++;
        }
        
        presenters = new Presenter[result.size()];
        return result.toArray(presenters);
    }
    
    
    public void setOnClickAction(Runnable action) {
        if(tree == null){
            return;
        }
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent arg0) {
                if(!getSelection(new Visitor_Swing()).isEmpty()){
                    action.run();
                }
            }
        });
    };
    
    @Override
    public Collection<DataElement> getSelection(Visitor visitor){
        DefaultMutableTreeNode element = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if(element == null){
            return new ArrayList<>();
        }
        Presenter presenter = (Presenter) element.getUserObject();
        ArrayList<DataElement> dataElements = new ArrayList<DataElement>();
        dataElements.addAll(presenter.visit(visitor));
        return dataElements;
    }
    
}
