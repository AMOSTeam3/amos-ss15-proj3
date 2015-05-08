package de.fau.osr.gui;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.fau.osr.bl.Tracker;
import de.fau.osr.core.db.CSVFileDataSource;
import de.fau.osr.core.db.DataSource;
import de.fau.osr.core.vcs.base.Commit;
import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.CommitState;
import de.fau.osr.core.vcs.base.VcsController;
import de.fau.osr.core.vcs.base.VcsEnvironment;
import de.fau.osr.util.parser.CommitMessageParser;
import de.fau.osr.util.parser.Parser;

public class GuiModell {
	
	Logger logger = LoggerFactory.getLogger(GuiModell.class);

	Tracker tracker;
	VcsController vcsController;
	DataSource dataSource;
	Pattern reqPattern;
	
	public GuiModell(File repoFile, String reqPatternString) throws PatternSyntaxException, IOException{
		Path repoPath = repoFile.toPath();
		vcsController = new VcsController(VcsEnvironment.GIT);

		if (vcsController.Connect(repoPath.toString())) {
			tracker = new Tracker(vcsController);
		} else {
			throw new RepositoryNotFoundException(repoPath.toString());
		}
		
		//TODO: This Path should not be hard coded
		Path dataSrcFilePath = repoPath.getParent().resolve("dataSource.csv");
		dataSource = new CSVFileDataSource(dataSrcFilePath.toFile());
		
		Pattern reqPattern = Pattern.compile(reqPatternString);
			
		this.reqPattern = reqPattern;
		
		
	}

	public String[] getAllFiles(){
		Set<String> files = new HashSet<String>();
		Iterator<String> allCommits = vcsController.getCommitList();
		while (allCommits.hasNext()) {
			String currentCommit = allCommits.next();
			for(CommitFile commitfile: vcsController.getCommitFiles(currentCommit)){
				String name;
				if(commitfile.commitState == CommitState.DELETED){
					name = commitfile.oldPath.getPath();
				}else{
					name = commitfile.newPath.getPath();
				}
				files.add(name);
			}
		}
			
		String[] filesArray = new String[files.size()];
		files.toArray(filesArray);
		return filesArray;
	}
	
	
	
	public ArrayList<Commit> getCommitsForRequirementID(String requirementID) {
		Parser parser = new CommitMessageParser();
		ArrayList<Commit> commits = new ArrayList<Commit>();

		Iterator<String> allCommits = vcsController.getCommitList();
		while (allCommits.hasNext()) {
			String currentCommit = allCommits.next();
			//TODO: parser is not using the input pattern
			if (parser.parse(vcsController.getCommitMessage(currentCommit))
					.contains(Integer.valueOf(requirementID))) {
				commits.add(new Commit(currentCommit, vcsController.getCommitMessage(currentCommit), null, vcsController.getCommitFiles(currentCommit)));
			}
		}
		
		for(String commitID: getRequirementCommitRelationFromDB(requirementID)){
			commits.add(new Commit(commitID, vcsController.getCommitMessage(commitID), null, vcsController.getCommitFiles(commitID)));
		}

		return commits;
	}
	
	
	/*
	 * Req-13
	 * Responsibility: Taleh
	 */
	private ArrayList<String> getRequirementCommitRelationFromDB(String requirementID) {
		ArrayList<String> rvalue = null;
		try {
			rvalue = Lists.newArrayList(dataSource.getCommitRelationByReq(Integer.valueOf(requirementID)));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rvalue;
	}

	
	/*
	 * Req-12
	 * Responsibility: Rajab
	 */
	public String[] getRequirementIDs(){
		ArrayList<String> requirements = new ArrayList<String>();
		requirements.add("0");
		requirements.add("1");
		requirements.add("2");
		requirements.add("3");
		requirements.add("4");
		requirements.add("5");
		requirements.add("6");
		requirements.add("7");
		String[] returntype = new String[requirements.size()];
		return requirements.toArray(returntype);
	}
}

