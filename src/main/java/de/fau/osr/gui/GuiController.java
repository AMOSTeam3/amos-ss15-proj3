package de.fau.osr.gui;

import de.fau.osr.util.AppProperties;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.PatternSyntaxException;


/*
 * Using a MVC-Pattern for the GUI.
 * The Controller class fetches the data from the Modell and passes it to the View.
 * Additional it eventually sets Events to the GUI-Elements, so that an action can call
 * the appropriate controller method, within which the data is fetched again ...
 */
public class GuiController {
	//Whether the user gets another chance to input correct data
	enum RetryStatus{Retry, Exit, Cancel}
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
					String reqPatternString = guiView.Pattern_OpeningDialog(AppProperties.GetValue("RequirementPattern"));
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
	void filesFromRequirement(String requirementID) throws IOException {
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
	void requirementsFromFile(String filePath) throws IOException {
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
		} catch (IOException e) {
			e.printStackTrace();
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
		// TODO Auto-generated method stub
		
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
	
	/*
	 *For reconfiguring the repository to a new path while the application is running
	 *Once this method is successful, the application refers to the new repository 
	 */
	
	void reConfigureRepository(){
		GuiModell guiModellTrial = guiModell;
		for(int i = 0; i<=MAX_RETRIES; i++){
			if(i == MAX_RETRIES){
				Status = RetryStatus.Cancel;
				guiView.showErrorDialog("Maximum retries exceeded");
				return;
			}
			File repoFile = null;
			try {
				repoFile = guiView.Repo_OpeningDialog();
			} catch (IOException e1) {
				
			}
			if(repoFile == null){
				Status = RetryStatus.Cancel;
				return;
			}
			try {
				guiModellTrial = new GUIModellFacadeAdapter(repoFile, guiModell.getCurrentRequirementPatternString());
				guiView.showInformationDialog("Repository Path modified to " + repoFile.getPath());
				break;
			} catch (IOException | RuntimeException e) {
				
				guiView.showErrorDialog(e.getMessage());
				handleError();
			}
		}
		guiModell = guiModellTrial;
		requirementsFromDB();
	}
	/*
	 * For reconfiguring the requirement pattern to a new pattern while the application is running
	 * Once this method is successful, the application refers to the new requirement pattern 
	 */
	void reConfigureRequirementPattern(){
		GuiModell guiModellTrial = guiModell;
		for(int i = 0; true; i++){
			if(i == MAX_RETRIES){
				Status = RetryStatus.Cancel;
				guiView.showErrorDialog("Maximum retries exceeded");
				return;
			}
			String reqPatternString = guiView.Pattern_OpeningDialog(guiModell.getCurrentRequirementPatternString());
			if(reqPatternString == null){
				Status = RetryStatus.Cancel;
				return;
			}
			try {
				guiModellTrial = new GUIModellFacadeAdapter(new File(guiModell.getCurrentRepositoryPath()), reqPatternString);
				guiView.showInformationDialog("Requirement Pattern modified to " + reqPatternString);
				break;
			} catch (RuntimeException | IOException e) {				
				guiView.showErrorDialog(e.getMessage());
				handleError();
			}
		}
		guiModell = guiModellTrial;
		requirementsFromDB();
		
	}
	
	/*
	 * method to divert configuration calls
	 */
	void reConfigure(){
		switch(guiView.Configure_OptionDialog())
		{
		// these values have to be replaced by some enums
		case 0:
			reConfigureRepository();
			break;
		case 1:
			reConfigureRequirementPattern();
			break;
		}
			
		
	}
}
