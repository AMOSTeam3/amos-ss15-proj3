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



public class GuiController {
	enum RetryStatus{Retry, Exit}
	
	private static final int MAX_RETRIES = 3;
	private RetryStatus Status;
	
	private ArrayList<Commit> commits;
	private List<CommitFile> commitFiles;
	
	GuiView guiView;
	GuiModell guiModell;

	public GuiController(){	
		Status = RetryStatus.Retry;
		
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				
				guiView = new GuiView();
				
				File repoFile;
				for(int i = 0; true; i++){
					try {
						repoFile = guiView.Repo_OpeningDialog();
						break;
					} catch (IOException e) {
						if(i >= MAX_RETRIES){
							Status = RetryStatus.Exit;
						}
						guiView.showErrorDialog(e.getMessage());
					}
				}
				
				for(int i = 0; true; i++){
					String reqPatternString = guiView.Pattern_OpeningDialog();
					try {
						guiModell = new GuiModell(repoFile, reqPatternString);
						break;
					} catch (PatternSyntaxException pse) {
						if(i >= MAX_RETRIES){
							Status = RetryStatus.Exit;
						}
						guiView.showErrorDialog(pse.getMessage());
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
				
				guiView.showView();
				initializeButtonActions();
				requirementsFromDB();
			}
		});
	}
	
	protected void initializeButtonActions() {
		guiView.initializeButtonActions(this);
	}

	void requirementsFromDB() {
		guiView.clearAllScrollPanes();
		
		String[] requirements = guiModell.getRequirementIDs();
		JList<String> requirements_JList = new JList<String>(requirements);
		guiView.showRequirements(requirements_JList);
		
		guiView.addMouseListener(requirements_JList, new MouseEvent(this, Action.commitsFromRequirement));
	}
	
	void commitsFromRequirement(String requirement) {
		
		commits = guiModell.getCommitsForRequirementID(requirement);
		JList<String> commitMessages_JList = new JList<String>(DataTransformer.getMessagesFromCommits(commits));
		guiView.showCommits(commitMessages_JList);
		
		guiView.addMouseListener(commitMessages_JList, new MouseEvent(this, Action.FilesFromCommit));
	}

	void filesFromCommit(int commitIndex) {
		commitFiles = commits.get(commitIndex).files;
		JList<String> commitFileName_JList = new JList<String>(DataTransformer.getNamesFromCommitFiles(commitFiles));
		guiView.showFiles(commitFileName_JList);
		
		guiView.addMouseListener(commitFileName_JList, new MouseEvent(this, Action.CodeFromFile));
	}

	void codeFromFile(int filesIndex) {
		String changeData = commitFiles.get(filesIndex).changedData;
		guiView.showCode(changeData);
	}

	void filesFromDB() {
		guiView.clearAllScrollPanes();
		
		JList<String> commitFileName_JList = new JList<String>(guiModell.getAllFiles());
		guiView.showFiles(commitFileName_JList);
	}
	
	void handleError(){
		if(Status == RetryStatus.Exit){
			System.exit(1);
		}
	}
}
