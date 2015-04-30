package de.fau.osr.core.vcs.interfaces;

import java.util.ArrayList;
import java.util.Iterator;

import de.fau.osr.core.vcs.base.CommitFile;

/**
 * @author Gayathery
 * 
 */
public interface VcsClient {

	public boolean connect();
	public Iterator<String> getBranchList();
	public Iterator<String> getCommitList();
	public ArrayList<CommitFile> getCommitFiles(String commitID);
	public String getCommitMessage(String commitID);
	public Iterator<String> getCommitListForFileodification(String filePath);
}
