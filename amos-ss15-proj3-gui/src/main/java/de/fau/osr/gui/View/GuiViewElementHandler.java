package de.fau.osr.gui.View;

import de.fau.osr.gui.Components.MultiSplitPane;
import de.fau.osr.gui.Controller.GuiController;
import de.fau.osr.gui.Controller.Transformer;
import de.fau.osr.gui.Controller.Visitor_Swing;
import de.fau.osr.gui.Model.DataElements.Commit;
import de.fau.osr.gui.Model.DataElements.DataElement;
import de.fau.osr.gui.Model.DataElements.Requirement;
import de.fau.osr.gui.View.ElementHandler.*;
import de.fau.osr.gui.util.filtering.FilterByExactString;






import javax.swing.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class GuiViewElementHandler extends JFrame {
    

    private static final long serialVersionUID = 1L;
    private GuiController guiController;

    private MenuHandler Menu_Handler = new MenuHandler();
    private Requirement_ElementHandler Requirement_Handler = new Requirement_ElementHandler();
    private Commit_ElementHandler Commit_Handler = new Commit_ElementHandler();
    private CommitFile_ElementHandler CommitFile_Handler = new CommitFile_ElementHandler();
    private Impact_ElementHandler Impact_Handler = new Impact_ElementHandler();
    private Code_ElementHandler Code_Handler = new Code_ElementHandler(Impact_Handler.getScrollPane());
    
    
    private Requirement_ElementHandler Requirement_HandlerRequirementTab = new Requirement_ElementHandler();
    private Requirement_Detail_ElementHandler Requirement_Detail_Handler = new Requirement_Detail_ElementHandler();
    
    private Requirement_ElementHandler Requirement_Handler_ManagementTab = new Requirement_ElementHandler();
    private Commit_ElementHandler Commit_Handler_ManagementTab = new Commit_ElementHandler();
    private Linkage_ElementHandler Linkage_Handler = new Linkage_ElementHandler();
    
    private JPanel mainNavigationPanel;
    private JPanel requirementModificationPanel;
    private JPanel LinkageManagmentPanel;

    public GuiViewElementHandler(GuiController guiController) {
        this.guiController = guiController;
        
        initializeButtonActions();
        initializeComboboxActions();
        
        setTitle("Spice Traceability");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBackground(Color.WHITE);
        setJMenuBar(Menu_Handler.getMenuBar());
        
        createTabs();
        positionMainPanelElements();
        positionRequirementPanelElements();
        positionRequirementManagementPanelElements();
        
        pack();
        setVisible(true);
    }

    public Requirement_ElementHandler getRequirement_ElementHandler() {
        return Requirement_Handler;
    }
    
    public Commit_ElementHandler getCommit_ElementHandler() {
        return Commit_Handler;
    }
    
    public CommitFile_ElementHandler getCommitFile_ElementHandler() {
        return CommitFile_Handler;
    }
    
    public Impact_ElementHandler getImpact_ElementHandler() {
        return Impact_Handler;
    }
    
    public Code_ElementHandler getCode_ElementHandler() {
        return Code_Handler;
    }
    
    public Linkage_ElementHandler getLinkage_ElementHandler() {
        return Linkage_Handler;
    }
    
    public Requirement_ElementHandler getRequirement_ElementHandlerRequirementTab() {
        return Requirement_HandlerRequirementTab;
    }
    
    public Requirement_Detail_ElementHandler getRequirement_Detail_ElementHandler() {
        return Requirement_Detail_Handler;
    }
    
    public Requirement_ElementHandler getRequirement_Handler_ManagementTab() {
        return Requirement_Handler_ManagementTab;
    }
    
    public Commit_ElementHandler getCommit_Handler_ManagementTab() {
        return Commit_Handler_ManagementTab;
    }
    
    public Collection<ElementHandler> getElementHandlers(){
        ArrayList<ElementHandler> elementHandlers = new ArrayList<ElementHandler>();
        elementHandlers.add(Requirement_Handler);
        elementHandlers.add(Commit_Handler);
        elementHandlers.add(CommitFile_Handler);
        elementHandlers.add(Impact_Handler);
        elementHandlers.add(Code_Handler);
        elementHandlers.add(Linkage_Handler);
        return elementHandlers;
    }
    
    private void createTabs(){
        mainNavigationPanel = new JPanel();
        mainNavigationPanel.setLayout(new BorderLayout());
        requirementModificationPanel = new JPanel();
        requirementModificationPanel.setLayout(new BorderLayout());
        LinkageManagmentPanel = new JPanel();
        LinkageManagmentPanel.setLayout(new BorderLayout());
        
        JTabbedPane tabpane = new JTabbedPane
        (JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT);
        
        tabpane.addTab("Navigation", mainNavigationPanel);
        tabpane.addTab("Requirements", requirementModificationPanel);
        tabpane.addTab("Linkage Management", LinkageManagmentPanel);
        this.add(tabpane);
    }

    private void positionMainPanelElements() {
        
        /* *layoutStructure* explained:
            2D-Array: 1st dimension ==> Columns, 2nd dimension ==> Rows

            {
                {Component01, Component02, ... }, # Column
                {Component11, Component12, ... }, # Column
            }

            ComponentXY: X-->Column-No, Y-->Row-No
        */
        // Based on this layout sturcture GUI will be created by using MultiSplinPane
        /*
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
        */

        ElementHandler[] elemHandlers = {
                Requirement_Handler, Commit_Handler, CommitFile_Handler, Impact_Handler, Code_Handler
        };
        MultiSplitPane pane = new MultiSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        for ( ElementHandler eachElemHandler: elemHandlers)
            pane.addComponent(eachElemHandler.toComponent());

        //setLayout(new BorderLayout());
        mainNavigationPanel.add(pane, BorderLayout.CENTER);

        /*

        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
                layout.createParallelGroup()
                        .addGroup(layout.createSequentialGroup()
										//here follow the columns of the UI
										.addGroup(Requirement_Handler.toHorizontalGroup(layout))
										.addGroup(Commit_Handler.toHorizontalGroup(layout))
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(Linkage_Handler.getButton())
														.addGroup(CommitFile_Handler.toHorizontalGroup(layout))
										)
										.addGroup(Impact_Handler.toHorizontalGroup(layout))
										.addGroup(Code_Handler.toHorizontalGroup(layout))
						)
        );

        layout.setVerticalGroup(
            layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
								//two rows, one to create linkage, the other one for the rest
								.addGroup(Linkage_Handler.toHorizontalGroup(layout))
								.addGroup(layout.createParallelGroup()
												.addGroup(Requirement_Handler.toVerticalGroup(layout))
												.addGroup(Commit_Handler.toVerticalGroup(layout))
												.addGroup(CommitFile_Handler.toVerticalGroup(layout))
												.addGroup(Impact_Handler.toVerticalGroup(layout))
												.addGroup(Code_Handler.toVerticalGroup(layout))
								)
				)
        );
        
        Requirement_Handler.linkSize(layout);

        setLayout(layout);
        */
    }
    
    private void positionRequirementPanelElements() {
        MultiSplitPane pane = new MultiSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        Requirement_HandlerRequirementTab = new Requirement_ElementHandler();
        Requirement_Detail_Handler = new Requirement_Detail_ElementHandler();
        pane.addComponent(Requirement_HandlerRequirementTab.toComponent());
        pane.addComponent(Requirement_Detail_Handler.toComponent());
        
        guiController.setRequirementIDFiltering(new FilterByExactString());
        
        Requirement_HandlerRequirementTab.setButtonAction(()->{
            guiController.requirementsFromDBForRequirementTab();
        });
        
        requirementModificationPanel.add(pane, BorderLayout.CENTER);
    }
    
    private void positionRequirementManagementPanelElements() {
        MultiSplitPane pane = new MultiSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        pane.addComponent(Requirement_Handler_ManagementTab.toComponent());
        pane.addComponent(Commit_Handler_ManagementTab.toComponent());
        pane.addComponent(Linkage_Handler.toComponent());
        
        Requirement_Handler_ManagementTab.setButtonAction(()->{
            //guiController.requirementsFromDBForRequirementTab();
        });
        
        Commit_Handler_ManagementTab.setButtonAction(()->{
            //guiController.requirementsFromDBForRequirementTab();
        });
        
        Linkage_Handler.setButtonAction(()->{
            //guiController.requirementsFromDBForRequirementTab();
        });
        
        LinkageManagmentPanel.add(pane, BorderLayout.CENTER);
    }

    void initializeButtonActions() {
        CommitFile_Handler.setButtonAction(()->guiController.filesFromDB());

        Requirement_Handler.setButtonAction(()->{
            guiController.requirementsFromDB();
        });

        Commit_Handler.setButtonAction(()->guiController.commitsFromDB());

        Linkage_Handler.setButtonAction(()->guiController.requirementsAndCommitsFromDB());

        Menu_Handler.setConfigureAction(() -> {
            try {
                guiController.reConfigure();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        Menu_Handler.setImpactAction(() -> {
            guiController.getTraceabilityMatrixByImpact();
        });
        
        /*DONOTREMOVE
        Menu_Handler.setByOtherDataAction(()->{
            guiController.getTraceabilityMatrix();
        });
        */
        
        guiController.setRequirementIDFiltering(new FilterByExactString());
        
        Requirement_Handler.setSearchTextFieldAction((RequirementSearch_textField)->{
            guiController.setRequirementIDFiltering(new FilterByExactString(RequirementSearch_textField.getText()));
        });
        
        Linkage_Handler.setButtonAction((Linkage_ButtonState)->{
            switch(Linkage_ButtonState){
            case Deactivate:
                guiController.requirementsAndCommitsFromDB();
                break;
            case Activate: 
                Requirement requirement = Linkage_Handler.getRequirement();
                Commit commit = Linkage_Handler.getCommit();
                if(requirement != null && commit != null){
                    guiController.addLinkage(requirement, commit);
                }
                break;
            }
        });
    }

    void initializeComboboxActions() {

        // Defining default selection.
        CommitFile_Handler.setComboBoxIndex(0);
        Runnable action = ()->guiController.setCommitFileSorting(CommitFile_Handler.getComboBoxValue());
        action.run();
        
        CommitFile_Handler.setComboBoxAction(action);
    }
}
