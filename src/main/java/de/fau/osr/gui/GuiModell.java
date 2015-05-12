package de.fau.osr.gui;

import java.io.FileNotFoundException;
import java.io.IOException;

/*
 * Adapter Pattern used. Since the Functionlibrary itself
 * can not simply provide the given return types correlating to
 * the specific formatted input, this class defines the Interface
 * to be provided by the adapter
 */

public interface GuiModell {

	String[] getAllRequirements();

	String[] getCommitsFromRequirementID(String requirement);

	String[] getAllFiles();

	String[] getRequirementsFromFile(String filePath) throws IOException;

	String[] getCommitsFromFile(String filePath);

	String[] getFilesFromCommit(int commitIndex) throws FileNotFoundException;

	String getChangeDataFromFileIndex(int filesIndex) throws FileNotFoundException;
	
	String getCurrentRequirementPatternString();
	
	String getCurrentRepositoryPath();

	String[] getCommitsFromDB();

	String[] getRequirementsFromCommit(int commitIndex) throws FileNotFoundException;

	String[] commitsFromRequirementAndFile(String requirementID,
			String filePath);

	String[] getRequirementsFromFileAndCommit(int commitIndex,
			String filePath) throws IOException;

	String[] getFilesFromRequirement(String requirementID) throws IOException;

	String[] commitsFromRequirementAndFile(String requirementID,
			int fileIndex) throws FileNotFoundException;

}
