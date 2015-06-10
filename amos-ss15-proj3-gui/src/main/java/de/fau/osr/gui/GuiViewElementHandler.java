package de.fau.osr.gui;

import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.gui.Components.javafx.ReqManagementPanel;
import de.fau.osr.gui.util.MultiSplitPane;
import de.fau.osr.util.filtering.FilterByExactString;
import de.fau.osr.util.sorting.SortByCommitID;
import de.fau.osr.util.sorting.SortByFilename;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class GuiViewElementHandler extends JFrame {
    public enum ButtonState{Deactivate, Activate}

    private static final long serialVersionUID = 1L;
    private GuiController guiController;

    private JLabel RequirementID_label = new JLabel("RequirementID");
    private JLabel Requirements2Lines_label = new JLabel("#");
    private JLabel Code_label = new JLabel("Code");
    private JLabel ImpactPercentage_label = new JLabel("Impact Percentage");
    private JLabel Commit_label = new JLabel("Commit");
    private JLabel Files_label = new JLabel("Files");

    private JButton Files_button = new JButton("Navigate From File");
    private JButton Commit_button = new JButton("Navigate From Commit");
    private JButton RequirementID_button = new JButton("Navigate From ID");
    private JButton Linkage_button = new JButton("Add Linkage");

    private JTextField RequirementID_textField = new JTextField();
    private JTextField Commit_textField = new JTextField();
    private JTextField RequirementSearch_textField = new JTextField();
    private JMenuBar menuBar = new JMenuBar();
    private JMenu mnTools = menuBar.add(new JMenu("Tools"));
    private JMenuItem mntmConfigure = mnTools.add(new JMenuItem("Configure"));
    private JMenuItem mntmTraceabilityMatrixByImpact = mnTools.add(new JMenuItem("Traceability Matrix By Impact"));
    //DONOTREMOVE private JMenuItem mntmTraceabilityMatrixByOtherData = mnTools.add(new JMenuItem("Traceability Matrix By Other Data"));
    private JMenu mnTraceabilityMatrix = new JMenu("TraceabilityMatrix");
    final private String[] SORT_COMBOBOX_CHOICES = {
            "sort by chronic", "sort by filename"
    };

    //this enables eclipse to use the WindowBuilder
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private JComboBox<String> FilesSort_combobox = new JComboBox(SORT_COMBOBOX_CHOICES);


    private JScrollPane RequirementID_scrollPane = new JScrollPane();
    private JScrollPane Commit_scrollPane = new JScrollPane();
    private JScrollPane Files_scrollPane = new JScrollPane();
    private JScrollPane Code_scrollPane = new JScrollPane();
    private JScrollPane Requirements2Lines_scrollPane = new JScrollPane();
    private JScrollPane ImpactPercentage_scrollPane = new JScrollPane();



    final private List<Comparator<CommitFile>> SORT_ALGORITHMS = Arrays.asList(
            new SortByCommitID(), new SortByFilename()
    );
    private JLabel FilesSort_label = new JLabel("Sort By:");

    public GuiViewElementHandler(GuiController guiController) {
        this.guiController = guiController;
        linkMenuItems();
        initializeButtonActions();
        initializeComboboxActions();
        setTitle("Spice Traceability");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBackground(Color.WHITE);
        setJMenuBar(menuBar);
        for(JTextField textField : new JTextField[]{RequirementID_textField, Commit_textField}) {
            textField.setEditable(false);
            textField.setColumns(10);
        }

        positionElements();

        pack();
    }

    public JScrollPane getRequirementID_scrollPane() {
        return RequirementID_scrollPane;
    }

    public JScrollPane getCommit_scrollPane() {
        return Commit_scrollPane;
    }

    public JScrollPane getFiles_scrollPane() {
        return Files_scrollPane;
    }
    public JScrollPane getRequirements2Lines_scrollPane() {
        return Requirements2Lines_scrollPane;
    }

    public JScrollPane getCode_scrollPane() {
        return Code_scrollPane;
    }

    public JScrollPane getImpactPercentage_scrollPane() {
        return ImpactPercentage_scrollPane;
    }

    public JTextField getRequirementID_textField() {
        return RequirementID_textField;
    }

    public JTextField getCommit_textField() {
        return Commit_textField;
    }

    private void positionElements() {
        /* *layoutStructure* explained:
            2D-Array: 1st dimension ==> Columns, 2nd dimension ==> Rows

            {
                {Component01, Component02, ... }, # Column
                {Component11, Component12, ... }, # Column
            }

            ComponentXY: X-->Column-No, Y-->Row-No
        */
        // Based on this layout sturcture GUI will be created by using MultiSplinPane
        Component[][] layoutStructure = {
                {RequirementID_textField, RequirementID_button, RequirementID_label, RequirementSearch_textField, RequirementID_scrollPane},
//              {Commit_textField, Commit_button, Commit_label, Commit_scrollPane},
                {Commit_textField,  Commit_label, Commit_scrollPane},
//              {Linkage_button, Files_button, FilesSort_combobox, Files_label, Files_scrollPane},
                {Linkage_button, Files_button, Files_label, Files_scrollPane},
                {Requirements2Lines_label, Requirements2Lines_scrollPane},
                {Code_label, Code_scrollPane}

        };

        MultiSplitPane pane = new MultiSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        for ( Component[] i: layoutStructure) {
            MultiSplitPane column = new MultiSplitPane(JSplitPane.VERTICAL_SPLIT, false);
            for (Component j: i)
                column.addComponent(j);
            pane.addComponent(column);
        }
        setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Main", pane);
        tabbedPane.addTab("Requirements management", new ReqManagementPanel());
        add(tabbedPane, BorderLayout.CENTER);

        //hide vertical scrollbar of req2line
        Requirements2Lines_scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        //synchronize vertical scrolling of code and req2line
        JScrollBar codeVertiSrollbar = Code_scrollPane.getVerticalScrollBar();
        JScrollBar req2lineVertiScrollbar = Requirements2Lines_scrollPane.getVerticalScrollBar();
        codeVertiSrollbar.setModel(req2lineVertiScrollbar.getModel());

    }

    void initializeButtonActions() {
        Files_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                guiController.filesFromDB();
            }
        });

        RequirementID_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    guiController.requirementsFromDB();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Commit_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                guiController.commitsFromDB();
            }
        });

        Linkage_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                guiController.requirementsAndCommitsFromDB();
            }
        });

        mntmConfigure.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    guiController.reConfigure();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mntmTraceabilityMatrixByImpact.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

                class TraceabilityMatrixViewerThread implements Runnable {

                    @Override
                    public void run() {
                        guiController.getTraceabilityMatrixByImpact();

                    }

                }
                Thread tr = new Thread(new TraceabilityMatrixViewerThread());
                tr.start();

            }
        });
        /*DONOTREMOVE
        mntmTraceabilityMatrixByOtherData.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

                class TraceabilityMatrixViewerThread implements Runnable{

                    @Override
                    public void run() {
                        guiController.getTraceabilityMatrix();

                    }

                }
                Thread tr = new Thread(new TraceabilityMatrixViewerThread());
                tr.start();



            }
        });
*/
        guiController.setRequirementIDFiltering(new FilterByExactString());
        RequirementSearch_textField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                guiController.setRequirementIDFiltering(new FilterByExactString(RequirementSearch_textField.getText()));
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                guiController.setRequirementIDFiltering(new FilterByExactString(RequirementSearch_textField.getText()));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                guiController.setRequirementIDFiltering(new FilterByExactString(RequirementSearch_textField.getText()));
            }
        });


    }

    void initializeComboboxActions() {

        // Defining default selection.
        FilesSort_combobox.setSelectedIndex(0);
        guiController.setCommitFileSorting(SORT_ALGORITHMS.get(0));

        FilesSort_combobox.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        guiController.setCommitFileSorting(SORT_ALGORITHMS.get(FilesSort_combobox.getSelectedIndex()));

                    }
                }
        );
    }

    void switchLinkageButton(ButtonState Linkage_ButtonState) {
        switch(Linkage_ButtonState){
        case Deactivate:
            Linkage_button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    guiController.requirementsAndCommitsFromDB();
                }
            });
            break;
        case Activate:
            Linkage_button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(!guiController.requirements_JList.isSelectionEmpty() && !guiController.commitMessages_JList.isSelectionEmpty()){
                        guiController.addLinkage(guiController.requirements_JList.getSelectedValue(), guiController.commitMessages_JList.getSelectedIndex());
                    }
                }
            });
            break;
        }
    }

    void linkMenuItems(){
        mnTraceabilityMatrix.add(mntmTraceabilityMatrixByImpact);
        //DONOTREMOVE mnTraceabilityMatrix.add(mntmTraceabilityMatrixByOtherData);
        mnTools.add(mnTraceabilityMatrix);
    }

}
