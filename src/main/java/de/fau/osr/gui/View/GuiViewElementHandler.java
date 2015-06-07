package de.fau.osr.gui.View;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import de.fau.osr.gui.Controller.GuiController;
import de.fau.osr.gui.Model.DataElements.Commit;
import de.fau.osr.gui.Model.DataElements.Requirement;
import de.fau.osr.gui.View.ElementHandler.Code_ElementHandler;
import de.fau.osr.gui.View.ElementHandler.CommitFile_ElementHandler;
import de.fau.osr.gui.View.ElementHandler.Commit_ElementHandler;
import de.fau.osr.gui.View.ElementHandler.ElementHandler;
import de.fau.osr.gui.View.ElementHandler.Impact_ElementHandler;
import de.fau.osr.gui.View.ElementHandler.Linkage_ElementHandler;
import de.fau.osr.gui.View.ElementHandler.MenuHandler;
import de.fau.osr.gui.View.ElementHandler.Requirement_ElementHandler;
import de.fau.osr.util.filtering.FilterByExactString;

public class GuiViewElementHandler extends JFrame {
    

    private static final long serialVersionUID = 1L;
    private GuiController guiController;

    private MenuHandler Menu_Handler = new MenuHandler();
    private Requirement_ElementHandler Requirement_Handler = new Requirement_ElementHandler();
    private Commit_ElementHandler Commit_Handler = new Commit_ElementHandler();
    private CommitFile_ElementHandler CommitFile_Handler = new CommitFile_ElementHandler();
    private Impact_ElementHandler Impact_Handler = new Impact_ElementHandler();
    private Code_ElementHandler Code_Handler = new Code_ElementHandler(Impact_Handler.getScrollPane());
    private Linkage_ElementHandler Linkage_Handler = new Linkage_ElementHandler(Requirement_Handler.getTextField(), Commit_Handler.getTextField());

    public GuiViewElementHandler(GuiController guiController) {
        this.guiController = guiController;
        
        initializeButtonActions();
        initializeComboboxActions();
        
        setTitle("Spice Traceability");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBackground(Color.WHITE);
        setJMenuBar(Menu_Handler.getMenuBar());

        positionElements();

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

    private void positionElements() {
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
    }

    void initializeButtonActions() {
        CommitFile_Handler.setButtonAction(()->guiController.filesFromDB());

        Requirement_Handler.setButtonAction(()->{
            guiController.requirementsFromDB();
        });

        Commit_Handler.setButtonAction(()->guiController.commitsFromDB());

        Linkage_Handler.setButtonAction(()->guiController.requirementsAndCommitsFromDB());

        Menu_Handler.setConfigureAction(()->{
                try {
                    guiController.reConfigure();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            
        });

        Menu_Handler.setImpactAction(()->{
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
