package de.fau.osr.gui;

import de.fau.osr.bl.RequirementsTraceabilityMatrix;
import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.gui.GuiView.HighlightedLine;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;


import org.eclipse.jgit.api.errors.GitAPIException;

import de.fau.osr.core.vcs.base.CommitFile;
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

	CommitFile[] getAllFiles(Comparator<CommitFile> sorting);

	String[] getRequirementsFromFile(CommitFile file) throws IOException;

	String[] getCommitsFromFile(CommitFile file);

	CommitFile[] getFilesFromCommit(int commitIndex, Comparator<CommitFile> sorting) throws FileNotFoundException;

	String getChangeDataFromFileIndex(CommitFile file) throws FileNotFoundException;
	
	String getCurrentRequirementPatternString();
	
	String getCurrentRepositoryPath();

	String[] getCommitsFromDB();

	String[] getRequirementsFromCommit(int commitIndex) throws IOException;

	String[] commitsFromRequirementAndFile(String requirementID,
			CommitFile file) throws IOException;

	String[] getRequirementsFromFileAndCommit(int commitIndex,
			CommitFile file) throws IOException;

	CommitFile[] getFilesFromRequirement(String requirementID, Comparator<CommitFile> sorting) throws IOException;
	
	void addRequirementCommitLinkage(String requirementID, int commitIndex) throws FileNotFoundException;

	HighlightedLine[] getBlame(CommitFile file, String requirementID) throws FileNotFoundException, IOException, GitAPIException ;
	
	String[] getRequirementsForBlame(int lineIndex, CommitFile file) throws FileNotFoundException, IOException, GitAPIException;
	
	public RequirementsTraceabilityMatrix getRequirementsTraceability() throws IOException;

}
