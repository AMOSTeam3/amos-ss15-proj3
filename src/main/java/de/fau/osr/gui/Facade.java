package de.fau.osr.gui;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.fau.osr.bl.Tracker;
import de.fau.osr.core.db.CSVFileDataSource;
import de.fau.osr.core.db.DataSource;
import de.fau.osr.core.vcs.base.Commit;
import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.CommitState;
import de.fau.osr.core.vcs.base.VcsEnvironment;
import de.fau.osr.core.vcs.interfaces.VcsClient;
import de.fau.osr.util.parser.CommitMessageParser;
import de.fau.osr.util.parser.Parser;

public class Facade {
	
	Logger logger = LoggerFactory.getLogger(Facade.class);

	Tracker tracker;
	VcsClient vcsClient;
	DataSource dataSource;
	Path repoPath;
	
	public Facade(File repoFile, String reqPatternString) throws IOException,RuntimeException{
		Path repoPath = repoFile.toPath();
		
		vcsClient = VcsClient.connect(VcsEnvironment.GIT, repoPath.toString());

		tracker = new Tracker(vcsClient);
		
		this.repoPath = repoPath;
		//TODO: This Path should not be hard coded
		Path dataSrcFilePath = repoPath.getParent().resolve("dataSource.csv");
		dataSource = new CSVFileDataSource(dataSrcFilePath.toFile());
		
		Pattern reqPattern = Pattern.compile(reqPatternString);
		CommitMessageParser.setPattern(reqPattern);
	}
	
	public Collection<String> getAllFiles(){
		Set<String> files = new HashSet<String>();
		Iterator<String> allCommits = vcsClient.getCommitList();
		while (allCommits.hasNext()) {
			String currentCommit = allCommits.next();
			for(CommitFile commitfile: vcsClient.getCommitFiles(currentCommit)){
				String name;
				if(commitfile.commitState == CommitState.DELETED){
					name = commitfile.oldPath.getPath();
				}else{
					name = commitfile.newPath.getPath();
				}
				Pattern pattern = Pattern.compile("src");
				Matcher m = pattern.matcher(name);
				if(m.find()){
					files.add(name);
				}
			}
		}
		return files;
	}
	
	
	
	public Collection<Commit> getCommitsForRequirementID(String requirementID) {
		Parser parser = new CommitMessageParser();
		ArrayList<Commit> commits = new ArrayList<Commit>();

		Iterator<String> allCommits = vcsClient.getCommitList();
		while (allCommits.hasNext()) {
			String currentCommit = allCommits.next();
			if (parser.parse(vcsClient.getCommitMessage(currentCommit))
					.contains(Integer.valueOf(requirementID))) {
				commits.add(new Commit(currentCommit, vcsClient.getCommitMessage(currentCommit), null, vcsClient.getCommitFiles(currentCommit)));
			}
		}
		
		for(String commitID: getRequirementCommitRelationFromDB(requirementID)){
			commits.add(new Commit(commitID, vcsClient.getCommitMessage(commitID), null, vcsClient.getCommitFiles(commitID)));
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
	public Collection<String> getAllRequirements(){
		ArrayList<String> requirements = new ArrayList<String>();
		requirements.add("0");
		requirements.add("1");
		requirements.add("2");
		requirements.add("3");
		requirements.add("4");
		requirements.add("5");
		requirements.add("6");
		requirements.add("7");
		return requirements;
	}

	public Collection<Integer> getRequirementsForFile(String filePath) {
		return tracker.getAllRequirementsforFile(filePath);
	}

	public Collection<Commit> getCommitsFromFile(String filePath) {
		Iterator<String> iterator = vcsClient.getCommitListForFileodification(filePath);
		ArrayList<Commit> commits = new ArrayList<Commit>();
		while(iterator.hasNext()){
			String Id = iterator.next();
			commits.add(new Commit(Id, vcsClient.getCommitMessage(Id), null, vcsClient.getCommitFiles(Id)));
					
		}
		return commits;
	}

	public Collection<Commit> getCommitsFromDB() {
		Iterator<String> iterator = vcsClient.getCommitList();
		ArrayList<Commit> commits = new ArrayList<Commit>();
		while(iterator.hasNext()){
			String Id = iterator.next();
			commits.add(new Commit(Id, vcsClient.getCommitMessage(Id), null, vcsClient.getCommitFiles(Id)));
					
		}
		return commits;
	}

	public Collection<Integer> getRequirementsFromCommit(Commit commit) {
		Parser parser = new CommitMessageParser();
		Collection<Integer> requirements = parser.parse(commit.message);
		return requirements;
	}

	public Collection<CommitFile> getFilesFromRequirement(String requirementID) {
		return tracker.getCommitFilesForRequirementID(requirementID);
	}
	
	/*
	 * method to get the current requirement pattern used by the implementation behind the facade
	 */
	public String getCurrentRequirementPatternString(){
		return CommitMessageParser.getPattern().toString();
	}
	
	/*
	 * method to get the current repository path  used by the implementation behind the facade
	 */
	public String getCurrentRepositoryPath(){
		return repoPath.toString();
	}
}

