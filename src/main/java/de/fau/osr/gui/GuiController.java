package de.fau.osr.gui;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.PatternSyntaxException;

import javax.swing.JList;

import de.fau.osr.gui.GuiViewElementHandler.ButtonState;


/*
 * Using a MVC-Pattern for the GUI.
 * The Controller class fetches the data from the Modell and passes it to the View.
 * Additional it eventually sets Events to the GUI-Elements, so that an action can call
 * the appropriate controller method, within which the data is fetched again ...
 */
public class GuiController {
	//Whether the user gets another chance to input correct data
	enum RetryStatus{Retry, Exit}
	private static final int MAX_RETRIES = 3;
	private RetryStatus Status;
	
	GuiView guiView;
	GuiModell guiModell;
	
	JList<String> requirements_JList;
	JList<String> commitMessages_JList;
	JList<String> commitFileName_JList;

	/*
	 * Called to start the initially starts the program. Setting up GUI and displaying the initial data:
	 * All Requirements from external tool/DB(jira...)
	 */
	public GuiController(){	
		Status = RetryStatus.Retry;
		
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				
				guiView = new GuiView();
				
				for(int i = 0; true; i++){
					File repoFile = null;
					try {
						repoFile = guiView.Repo_OpeningDialog();
					} catch (IOException e1) {
						System.exit(0);
					}
					String reqPatternString = guiView.Pattern_OpeningDialog();
					try {
						guiModell = new GUIModellFacadeAdapter(repoFile, reqPatternString);
						break;
					} catch (PatternSyntaxException | IOException e) {
						if(i >= MAX_RETRIES){
							Status = RetryStatus.Exit;
						}
						guiView.showErrorDialog(e.getMessage());
						handleError();
					}
				}
				
				guiView.showView();
				initializeButtonActions();
				requirementsFromDB();
			}
		});
	}
	
	/*
	 * Every Event should cause a method in this controller to run. Therefore,
	 * whereever a action is defined, the Controller must be available. This 
	 * function passes the controller-element to the ElementHandler
	 */
	protected void initializeButtonActions() {
		guiView.initializeButtonActions(this);
	}

	
	
	/*
	 * Navigation: ->Requirements
	 * Clear: All
	 * Setting: Requirements
	 * Using: getAllRequirements
	 */
	void requirementsFromDB() {
		guiView.clearAll();
		
		String[] requirements = guiModell.getAllRequirements();
		requirements_JList = new JList<String>(requirements);
		guiView.showRequirements(requirements_JList);
		
		guiView.addMouseListener(requirements_JList, new MouseEvent(this, Action.CommitsAndFilesFromRequirement));
	}
	
	/*
	 * Navigation: ->Requirements->Commit
	 * Clear: Files/Code/ImpactPercentage
	 * Setting: Commits
	 * Using: getCommitsFromRequirementID
	 */
	void commitsFromRequirement(String requirement) {
		guiView.clearFiles();
		guiView.clearCode();
		guiView.clearImpactPercentage();
		
		commitMessages_JList = new JList<String>(guiModell.getCommitsFromRequirementID(requirement));
		guiView.showCommits(commitMessages_JList);
		
		guiView.addMouseListener(commitMessages_JList, new MouseEvent(this, Action.FilesFromCommit));
	}

	/*
	 * Navigation: ->Requirements->File
	 * Clear: Code/ImpactPercentage
	 * Setting: Files
	 * Using: getFilesFromRequirement
	 */
	void filesFromRequirement(String requirementID) {
		guiView.clearCode();
		guiView.clearImpactPercentage();
		
		commitFileName_JList = new JList<String>(guiModell.getFilesFromRequirement(requirementID));
		guiView.showFiles(commitFileName_JList);
		
		guiView.addMouseListener(commitFileName_JList, new MouseEvent(this, Action.CommitsAndCodeFromRequirementAndFile));
		
	}
	
	/*
	 * Navigation: ->Requirements->File->Commit
	 * Clear: 
	 * Setting: Commits
	 * Using: commitsFromRequirementAndFile
	 */
	void commitsFromRequirementAndFile(String requirementID, int fileIndex) {
		try {
			commitMessages_JList = new JList<String>(guiModell.commitsFromRequirementAndFile(requirementID, fileIndex));
		} catch (FileNotFoundException e) {
			guiView.showErrorDialog("Internal storing Error");
			return;
		}
		guiView.showCommits(commitMessages_JList);
	}
	
	


	
	/*
	 * Navigation: ->Files->Code
	 * Clear: ImpactPercentage
	 * Setting: Code
	 * Using: getChangeDataFromFileIndex
	 */
	void codeFromFile(int filesIndex) {
		guiView.clearImpactPercentage();
		
		String changeData;
		try {
			changeData = guiModell.getChangeDataFromFileIndex(filesIndex);
		} catch (FileNotFoundException e) {
			guiView.showErrorDialog("Internal storing Error");
			return;
		}
		guiView.showCode(changeData);
	}

	/*
	 * Navigation: ->Files
	 * Clear: All
	 * Setting: Files
	 * Using: getAllFiles
	 */
	void filesFromDB() {
		guiView.clearAll();
		
		commitFileName_JList = new JList<String>(guiModell.getAllFiles());
		guiView.showFiles(commitFileName_JList);
		
		guiView.addMouseListener(commitFileName_JList, new MouseEvent(this, Action.RequirementsAndCommitsFromFile));
	}

	/*
	 * Navigation: ->File->Requirement
	 * Clear: 
	 * Setting: Requirement
	 * Using: getRequirementsFromFile
	 */
	void requirementsFromFile(String filePath) {
		requirements_JList = new JList<String>(guiModell.getRequirementsFromFile(filePath));
		guiView.showRequirements(requirements_JList);
		
		guiView.addMouseListener(requirements_JList, new MouseEvent(this, Action.CommitsFromRequirementAndFile));
	}
	
	/*
	 * Navigation: ->File->Commit
	 * Clear: 
	 * Setting: Commits
	 * Using: getCommitsFromFile
	 */
	void commitsFromFile(String filePath){
		commitMessages_JList = new JList<String>(guiModell.getCommitsFromFile(filePath));
		guiView.showCommits(commitMessages_JList);
		
		guiView.addMouseListener(commitMessages_JList, new MouseEvent(this, Action.RequirementsFromFileAndCommit));
	}

	/*
	 * Navigation: ->File->Requirement->Commit
	 * Clear: 
	 * Setting: Commits
	 * Using: commitsFromRequirementAndFile
	 */
	void commitsFromRequirementAndFile(String requirementID, String filePath) {
		commitMessages_JList = new JList<String>(guiModell.commitsFromRequirementAndFile(requirementID, filePath));
		guiView.showCommits(commitMessages_JList);
	}

	/*
	 * Navigation: ->Files->Commit->Requirement
	 * Clear: 
	 * Setting: Requirement
	 * Using: getRequirementsFromFileAndCommit
	 */
	void requirementsFromFileAndCommit(int commitIndex, String filePath) {
		try {
			requirements_JList = new JList<String>(guiModell.getRequirementsFromFileAndCommit(commitIndex, filePath));
		} catch (FileNotFoundException e) {
			guiView.showErrorDialog("Internal storing Error");
			return;
		}
		guiView.showRequirements(requirements_JList);
		
	}

	
	
	
	/*
	 * Navigation: ->Commits
	 * Clear: All
	 * Setting: Commits
	 * Using: getCommitsFromDB
	 */
	void commitsFromDB() {
		guiView.clearAll();
		
		commitMessages_JList = new JList<String>(guiModell.getCommitsFromDB());
		guiView.showCommits(commitMessages_JList);
		
		guiView.addMouseListener(commitMessages_JList, new MouseEvent(this, Action.RequirementsAndFilesFromCommit));
	}
	
	/*
	 * Navigation: ->Commit->Files
	 * Clear: Code/ImpactPercentage
	 * Setting: Files
	 * Using: getFilesFromCommit
	 */
	void filesFromCommit(int commitIndex) {
		guiView.clearCode();
		guiView.clearImpactPercentage();
		
		
		try {
			commitFileName_JList = new JList<String>(guiModell.getFilesFromCommit(commitIndex));
		} catch (FileNotFoundException e) {
			guiView.showErrorDialog("Internal storing Error");
			return;
		}
		guiView.showFiles(commitFileName_JList);
		
		guiView.addMouseListener(commitFileName_JList, new MouseEvent(this, Action.CodeFromFile));
	}

	/*
	 * Navigation: ->Commits->Requirements
	 * Clear: 
	 * Setting: Requirements
	 * Using: getRequirementsFromCommit
	 */
	void requirementsFromCommit(int commitIndex) {
		try {
			requirements_JList = new JList<String>(guiModell.getRequirementsFromCommit(commitIndex));
		} catch (FileNotFoundException e) {
			guiView.showErrorDialog("Internal storing Error");
			return;
		}
		guiView.showRequirements(requirements_JList);
	}
	
	/*
	 * For button AddLinkage
	 */
	void requirementsAndCommitsFromDB() {
		guiView.clearAll();
		
		String[] requirements = guiModell.getAllRequirements();
		requirements_JList = new JList<String>(requirements);
		guiView.showRequirements(requirements_JList);
		guiView.addMouseListener(requirements_JList, new MouseEvent(this, Action.RequirementToLinkage));
		
		commitMessages_JList = new JList<String>(guiModell.getCommitsFromDB());
		guiView.showCommits(commitMessages_JList);
		guiView.addMouseListener(commitMessages_JList, new MouseEvent(this, Action.CommitToLinkage));
		
		guiView.switchLinkage_Button(ButtonState.Activate);
	}

	void RequirementToLinkage(String requirementID) {
		guiView.showLinkageRequirement(requirementID);
	}

	void CommitToLinkage(String commit) {
		guiView.showLinkageCommit(commit);
	}

	
	/*
	 * For now only terminated the application if the user retried some input to often.
	 * Later on should handle all actions that have to be completed before exit.
	 */
	void handleError(){
		if(Status == RetryStatus.Exit){
			System.exit(1);
		}
	}

	void addLinkage(String requirementID, int commitIndex) {
		try {
			guiModell.addRequirementCommitLinkage(requirementID, commitIndex);
			guiView.showInformationDialog("Successfully Added!");
		} catch (FileNotFoundException e) {
			guiView.showErrorDialog("Internal storing Error");
			return;
		}finally{
			guiView.clearAll();
		}
	}
}
