package de.fau.osr.gui;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.PatternSyntaxException;

import javax.swing.JList;


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
	 * Fetching all Requirements from external source (tool/DB) and displays them
	 * to the user
	 */
	void requirementsFromDB() {
		guiView.clearAllScrollPanes();
		
		String[] requirements = guiModell.getAllRequirements();
		JList<String> requirements_JList = new JList<String>(requirements);
		guiView.showRequirements(requirements_JList);
		
		guiView.addMouseListener(requirements_JList, new MouseEvent(this, Action.commitsFromRequirement));
	}
	
	/*
	 * Called when the user clicks on a requirementID. All corresponding
	 * Commits, where this requirements was mentioned and additionally all
	 * extra defined relationships are fetched from the Modell and shown.
	 */
	void commitsFromRequirement(String requirement) {
		JList<String> commitMessages_JList = new JList<String>(guiModell.getCommitsFromRequirementID(requirement));
		guiView.showCommits(commitMessages_JList);
		
		guiView.addMouseListener(commitMessages_JList, new MouseEvent(this, Action.FilesFromCommit));
	}

	/*
	 * Called when clicked on one commit. All file changes should be displayed
	 */
	void filesFromCommit(int commitIndex) {
		JList<String> commitFileName_JList;
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
	 * Called when clicked on a specific file change. Displays the corresponding diff
	 */
	void codeFromFile(int filesIndex) {
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
	 * Called if the user clicks on the "show all" button below the file label. Meaning, he wants's
	 * to navigate the other way round. Chosing one specific file getting the affected requirements ...
	 * This Method displays all files/paths to the user, which where ever touched in the repo history
	 */
	void filesFromDB() {
		guiView.clearAllScrollPanes();
		
		JList<String> commitFileName_JList = new JList<String>(guiModell.getAllFiles());
		guiView.showFiles(commitFileName_JList);
		
		guiView.addMouseListener(commitFileName_JList, new MouseEvent(this, Action.RequirementsFromFile));
	}

	/*
	 * When the user clicks on one specific file display from "filesFromDB",
	 * all requirements who ever influenced this file should be displayed to the user.
	 * This is the responsibility of this method
	 */
	void requirementsFromFile(String filePath) {
		JList<String> requirements = new JList<String>(guiModell.getRequirementsFromFile(filePath));
		guiView.showRequirements(requirements);
		
		guiView.addMouseListener(requirements, new MouseEvent(this, Action.CommitsFromRequirementAndFile));
	}
	
	/*
	 * Called together with method requirementsFromFile. Displaying all Commits, which ever touched the choosen
	 * file.
	 */
	void commitsFromFile(String filePath){
		JList<String> commits = new JList<String>(guiModell.getCommitsFromFile(filePath));
		guiView.showCommits(commits);
		
		guiView.addMouseListener(commits, new MouseEvent(this, Action.RequirementsFromFileAndCommit));
	}

	void commitsFromRequirementAndFile(String requirementID) {
		// TODO Auto-generated method stub
		
	}

	void requirementsFromFileAndCommit(String commitID) {
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
}
