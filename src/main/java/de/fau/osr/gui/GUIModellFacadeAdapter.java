package de.fau.osr.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.PatternSyntaxException;

import de.fau.osr.core.vcs.base.Commit;
import de.fau.osr.core.vcs.base.CommitFile;

/*
 * Adapter class. Providing the correct formatted input for the Library Facade and transforming
 * the output to match the needed Interface.
 */
public class GUIModellFacadeAdapter implements GuiModell {
	Facade facade;
	private Collection<CommitFile> commitFiles;
	private Collection<Commit> commits;

	public GUIModellFacadeAdapter(File repoFile, String reqPatternString) throws PatternSyntaxException, IOException{
		facade = new Facade(repoFile, reqPatternString);
	}

	@Override
	public String[] getAllRequirements() {
		Collection <String> collection = facade.getAllRequirements();
		return convertCollectionToArray(collection);
	}

	@Override
	public String[] getCommitsFromRequirementID(String requirement) {
		commits = facade.getCommitsForRequirementID(requirement);
		String[] commitMessagesArray = new String[commits.size()];
		int i = 0;
		for(Commit commit: commits){
			commitMessagesArray[i] = commit.message;
			i++;
		}
		return commitMessagesArray;
	}

	@Override
	public String[] getAllFiles() {
		Collection <String> collection = facade.getAllFiles();
		return convertCollectionToArray(collection);
	}

	@Override
	public String[] getRequirementsFromFile(String filePath) {
		String filePathTransformed = filePath.replace("\\", "/");
		Collection<Integer> requirements = facade.getRequirementsForFile(filePathTransformed);
		String[] requirementArray = new String[requirements.size()];
		int i = 0;
		for(Integer requirement: requirements){
			requirementArray[i] = requirement.toString();
			i++;
		}
		return requirementArray;
	}

	@Override
	public String[] getCommitsFromFile(String filePath) {
		String filePathTransformed = filePath.replace("\\", "/");
		Iterator<String> iterator = facade.getCommitsFromFile(filePathTransformed);
		ArrayList<String> commits = new ArrayList<String>();
		while(iterator.hasNext()){
			commits.add(iterator.next());
		}
		return convertCollectionToArray(commits);
	}

	public String[] getFilesFromCommit(int commitIndex) throws FileNotFoundException {
		String[] array;
		int i = 0;
		for(Commit commit: commits){
			if(i == commitIndex){
				array = new String[commit.files.size()];
				int j= 0;
				for(CommitFile commitfile: commit.files){
					array[j++] = commitfile.oldPath + " "
							+ commitfile.commitState + " "
							+ commitfile.newPath;
				}
				commitFiles = commit.files;
				return array;
			}
			i++;
		}
		throw new FileNotFoundException();
	}

	@Override
	public String getChangeDataFromFileIndex(int filesIndex) throws FileNotFoundException {
		int i = 0;
		for(CommitFile commitFile: commitFiles){
			if(i == filesIndex){
				return commitFile.changedData;
			}
			i++;
		}
		throw new FileNotFoundException();
	}
	
	
	private String[] convertCollectionToArray(Collection<String> collection){
		String[] array = new String[collection.size()];
		collection.toArray(array);
		return array;
	}
}
