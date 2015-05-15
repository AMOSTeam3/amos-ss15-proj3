package de.fau.osr.gui;

import de.fau.osr.bl.RequirementsTraceabilityMatrix;
import de.fau.osr.core.vcs.base.Commit;
import de.fau.osr.core.vcs.base.CommitFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;

import javax.swing.ListModel;

import org.eclipse.jgit.api.errors.GitAPIException;

import de.fau.osr.gui.GuiView.HighlightedLine;

/*
 * Adapter Pattern used. Since the Functionlibrary itself
 * can not simply provide the given return types correlating to
 * the specific formatted input, this class defines the Interface
 * to be provided by the adapter
 */

public interface GuiModel {

	String[] getAllRequirements() throws IOException;

	String[] getCommitsFromRequirementID(String requirement) throws IOException;

	String[] getAllFiles(Comparator<CommitFile> sorting);

	String[] getRequirementsFromFile(String filePath) throws IOException;

	String[] getCommitsFromFile(String filePath);

	String[] getFilesFromCommit(int commitIndex, Comparator<CommitFile> sorting) throws FileNotFoundException;

	String getChangeDataFromFileIndex(int filesIndex) throws FileNotFoundException;
	
	String getCurrentRequirementPatternString();
	
	String getCurrentRepositoryPath();

	String[] getCommitsFromDB();

	String[] getRequirementsFromCommit(int commitIndex) throws FileNotFoundException;

	String[] commitsFromRequirementAndFile(String requirementID,
			String filePath) throws IOException;

	String[] getRequirementsFromFileAndCommit(int commitIndex,
			String filePath) throws IOException;

	String[] getFilesFromRequirement(String requirementID, Comparator<CommitFile> sorting) throws IOException;

	String[] commitsFromRequirementAndFile(String requirementID,
			int fileIndex) throws IOException;
	
	void addRequirementCommitLinkage(String requirementID, int commitIndex) throws FileNotFoundException;

	HighlightedLine[] getBlame(int filesIndex, String requirementID) throws FileNotFoundException, IOException, GitAPIException ;
	
	String[] getRequirementsForBlame(int lineIndex, int filesIndex) throws FileNotFoundException, IOException, GitAPIException;
	
	public RequirementsTraceabilityMatrix getRequirementsTraceability() throws IOException;

}
