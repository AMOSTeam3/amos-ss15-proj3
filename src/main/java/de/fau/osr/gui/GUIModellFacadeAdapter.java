package de.fau.osr.gui;

import de.fau.osr.core.vcs.base.Commit;
import de.fau.osr.core.vcs.base.CommitFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/*
 * Adapter class. Providing the correct formatted input for the Library Facade and transforming
 * the output to match the needed Interface.
 */
public class GUIModellFacadeAdapter implements GuiModell {
	Facade facade;
	private Collection<CommitFile> commitFiles;
	private Collection<Commit> commits;

	public GUIModellFacadeAdapter(File repoFile, String reqPatternString)
			throws IOException, RuntimeException {
		facade = new Facade(repoFile, reqPatternString);
	}

	@Override
	public String[] getAllRequirements() {
		Collection<String> collection = facade.getAllRequirements();
		return convertCollectionToArray(collection);
	}

	@Override
	public String[] getCommitsFromRequirementID(String requirement) {
		commits = facade.getCommitsForRequirementID(requirement);
		return getMessagesFromCommits();
	}

	@Override
	public String[] getAllFiles() {
		Collection<String> collection = facade.getAllFiles();
		return convertCollectionToArray(collection);
	}

	@Override
	public String[] getRequirementsFromFile(String filePath) throws IOException {
		String filePathTransformed = filePath.replace("\\", "/");
		Collection<Integer> requirements = facade
				.getRequirementsForFile(filePathTransformed);
		return convertCollectionIntToArray(requirements);
	}

	String[] convertCollectionIntToArray(Collection<Integer> requirements) {
		String[] requirementArray = new String[requirements.size()];
		int i = 0;
		for (Integer requirement : requirements) {
			requirementArray[i] = requirement.toString();
			i++;
		}
		return requirementArray;
	}

	@Override
	public String[] getCommitsFromFile(String filePath) {
		String filePathTransformed = filePath.replace("\\", "/");
		commits = facade.getCommitsFromFile(filePathTransformed);
		return getMessagesFromCommits();
	}

	public String[] getFilesFromCommit(int commitIndex)
			throws FileNotFoundException {
		commitFiles = getCommit(commitIndex).files;
		return getCommitFileName();
	}

	@Override
	public String getChangeDataFromFileIndex(int filesIndex)
			throws FileNotFoundException {

		return getCommitFile(filesIndex).changedData;
	}

	@Override
	public String[] getCommitsFromDB() {
		commits = facade.getCommitsFromDB();
		return getMessagesFromCommits();
	}

	@Override
	public String[] getRequirementsFromCommit(int commitIndex)
			throws FileNotFoundException {
		Set<Integer> collection = new HashSet<Integer>(facade
				.getRequirementsFromCommit(getCommit(commitIndex)));
		return convertCollectionIntToArray(collection);
	}

	@Override
	public String[] commitsFromRequirementAndFile(String requirementID,
			String filePath){
		Set<String> commits1 = new HashSet<String>(Arrays.asList(getCommitsFromFile(filePath)));
		Set<String> commits2 = new HashSet<String>(Arrays.asList(getCommitsFromRequirementID(requirementID)));
		
		commits1.retainAll(commits2);
		return convertCollectionToArray(commits1);
	}

	@Override
	public String[] commitsFromRequirementAndFile(String requirementID,
			int fileIndex) throws FileNotFoundException {
		Collection<Commit> AllCommits = facade.getCommitsForRequirementID(requirementID);
		CommitFile commitFile = getCommitFile(fileIndex);
		for(Commit commit: AllCommits){
			for(CommitFile commitFilecompare: commit.files){
				if(commitFile.equals(commitFilecompare)){
					commits = new ArrayList<Commit>();
					commits.add(commit);
					return getMessagesFromCommits();
				}
			}
		}
		throw new FileNotFoundException();
	}

	@Override
	public String[] getRequirementsFromFileAndCommit(int commitIndex,
			String filePath) throws IOException {
		Set<String> requirements1 = new HashSet<String>(Arrays.asList(getRequirementsFromCommit(commitIndex)));
		Set<String> requirements2 = new HashSet<String>(Arrays.asList(getRequirementsFromFile(filePath)));
		
		requirements1.retainAll(requirements2);
		return convertCollectionToArray(requirements1);
	}

	@Override
	public String[] getFilesFromRequirement(String requirementID) throws IOException {
		commitFiles = facade.getFilesFromRequirement(requirementID);
		return getCommitFileName(); 
	}

	private CommitFile getCommitFile(int filesIndex)
			throws FileNotFoundException {
		int i = 0;
		for (CommitFile commitFile : commitFiles) {
			if (i == filesIndex) {
				return commitFile;
			}
			i++;
		}
		throw new FileNotFoundException();
	}

	private Commit getCommit(int commitIndex) throws FileNotFoundException {
		int i = 0;
		for (Commit commit : commits) {
			if (i == commitIndex) {
				return commit;
			}
			i++;
		}
		throw new FileNotFoundException();
	}

	private String[] convertCollectionToArray(Collection<String> collection) {
		String[] array = new String[collection.size()];
		collection.toArray(array);
		return array;
	}

	private String[] getMessagesFromCommits() {
		String[] commitMessagesArray = new String[commits.size()];
		int i = 0;
		for (Commit commit : commits) {
			commitMessagesArray[i] = commit.message;
			i++;
		}
		return commitMessagesArray;
	}

	private String[] getCommitFileName() {
		String[] array;
		array = new String[commitFiles.size()];
		int j = 0;
		for (CommitFile commitfile : commitFiles) {
			array[j++] = commitfile.oldPath + " " + commitfile.commitState
					+ " " + commitfile.newPath;
		}
		
		return array;
	}

	@Override
	public String getCurrentRequirementPatternString() {
		return facade.getCurrentRequirementPatternString();
	}

	@Override
	public String getCurrentRepositoryPath() {
		return facade.getCurrentRepositoryPath();
	}
	
}
