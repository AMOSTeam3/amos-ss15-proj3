package de.fau.osr.gui;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import javax.swing.JList;

import de.fau.osr.core.vcs.base.Commit;
import de.fau.osr.core.vcs.base.CommitFile;


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
	
	private ArrayList<Commit> commits;
	private List<CommitFile> commitFiles;
	
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
						guiModell = new GuiModell(repoFile, reqPatternString);
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
		
		String[] requirements = guiModell.getRequirementIDs();
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
		
		commits = guiModell.getCommitsForRequirementID(requirement);
		JList<String> commitMessages_JList = new JList<String>(DataTransformer.getMessagesFromCommits(commits));
		guiView.showCommits(commitMessages_JList);
		
		guiView.addMouseListener(commitMessages_JList, new MouseEvent(this, Action.FilesFromCommit));
	}

	/*
	 * Called when clicked on one commit. All file changes should be displayed
	 */
	void filesFromCommit(int commitIndex) {
		commitFiles = commits.get(commitIndex).files;
		JList<String> commitFileName_JList = new JList<String>(DataTransformer.getNamesFromCommitFiles(commitFiles));
		guiView.showFiles(commitFileName_JList);
		
		guiView.addMouseListener(commitFileName_JList, new MouseEvent(this, Action.CodeFromFile));
	}

	/*
	 * Called when clicked on a specific file change. Displays the corresponding diff
	 */
	void codeFromFile(int filesIndex) {
		String changeData = commitFiles.get(filesIndex).changedData;
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
