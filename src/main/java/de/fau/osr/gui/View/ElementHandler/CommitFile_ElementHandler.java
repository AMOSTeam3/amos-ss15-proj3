package de.fau.osr.gui.View.ElementHandler;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import de.fau.osr.gui.Controller.Visitor;
import de.fau.osr.gui.Model.DataElements.CommitFile;
import de.fau.osr.gui.Model.DataElements.DataElement;
import de.fau.osr.gui.View.Presenter.Presenter;
import de.fau.osr.gui.View.Renderer.Tree_Renderer;
import de.fau.osr.util.sorting.SortByCommitID;
import de.fau.osr.util.sorting.SortByFilename;

public class CommitFile_ElementHandler extends ElementHandler {
    
    private JLabel Files_label = new JLabel("Files");
    private JTree tree;
    
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
    
    public void setScrollPane_Content(JComponent elements){
        tree  = (JTree) elements;
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
    
    @Override
    public Collection<DataElement> getSelection(Visitor visitor){
        DefaultMutableTreeNode element = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        Presenter presenter = (Presenter) element.getUserObject();
        ArrayList<DataElement> dataElements = new ArrayList<DataElement>();
        dataElements.add(presenter.visit(visitor));
        return dataElements;
    }
    
}
