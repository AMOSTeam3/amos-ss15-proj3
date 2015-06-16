package de.fau.osr.gui.View.ElementHandler;

import de.fau.osr.gui.Controller.Visitor;
import de.fau.osr.gui.Model.DataElements.CommitFile;
import de.fau.osr.gui.Model.DataElements.DataElement;
import de.fau.osr.gui.View.Presenter.Presenter;
import de.fau.osr.gui.View.Renderer.Tree_Renderer;
import de.fau.osr.gui.View.CommitFile_SelectionListener;
import de.fau.osr.gui.Components.CommitFilesJTree;
import de.fau.osr.gui.util.sorting.SortByCommitID;
import de.fau.osr.gui.util.sorting.SortByFilename;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.*;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class CommitFile_ElementHandler extends ElementHandler {
    
    private JLabel Files_label = new JLabel("Files");
    private CommitFilesJTree tree;
    
    final private String[] SORT_COMBOBOX_CHOICES = {
            "sort by chronic", "sort by filename"
    };

    //this enables eclipse to use the WindowBuilder
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private JComboBox<String> FilesSort_combobox = new JComboBox(SORT_COMBOBOX_CHOICES);
    
    final private List<Comparator<CommitFile>> SORT_ALGORITHMS = Arrays.asList(
            new SortByCommitID(), new SortByFilename()
    );
    
    public CommitFile_ElementHandler(){
        button = new JButton("Navigate From File");
        scrollPane = new JScrollPane();
    }
    
    public void setComboBoxAction(Runnable action){
        FilesSort_combobox.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        action.run();
                    }
                }
        );
    }
    
    public void setComboBoxIndex(int i){
        FilesSort_combobox.setSelectedIndex(i);
    }
    
    public Comparator<CommitFile> getComboBoxValue(){
        return SORT_ALGORITHMS.get(FilesSort_combobox.getSelectedIndex());
    }
    
    public ParallelGroup toHorizontalGroup(GroupLayout layout) {
        return layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(button).addComponent(FilesSort_combobox)
                .addComponent(Files_label)
                .addComponent(scrollPane, 10, 100, Short.MAX_VALUE);
    }

    @Override
    public SequentialGroup toVerticalGroup(GroupLayout layout) {
        return layout.createSequentialGroup()
                .addComponent(button)
                .addComponent(FilesSort_combobox, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(Files_label)
                .addComponent(scrollPane);
    }
    
    public void setScrollPane_Content(Presenter[] elements, Runnable action){
        tree  = new CommitFilesJTree(elements);
      //expand all nodes
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
        
        Tree_Renderer renderer = new Tree_Renderer();
        tree.setCellRenderer(renderer);
        
        tree.addTreeSelectionListener(new CommitFile_SelectionListener(tree, action));
        
        JPanel panel = new JPanel(new GridLayout());
        panel.add(tree);
        scrollPane.setViewportView(panel);
    }
    
    @Override
    public Collection<DataElement> getSelection(Visitor visitor){
        DefaultMutableTreeNode element = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        Presenter presenter = (Presenter) element.getUserObject();
        ArrayList<DataElement> dataElements = new ArrayList<DataElement>();
        dataElements.add(presenter.visit(visitor));
        return dataElements;
    }
    
}
