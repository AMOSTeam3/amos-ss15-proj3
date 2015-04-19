package main.java.de.fau.osr.core.vcs.interfaces;

import java.util.Iterator;

import main.java.de.fau.osr.core.vcs.base.CommitFile;

/**
 * @author Gayathery
 * 
 */
public interface VcsClient {

	public boolean connect();
	public Iterator<String> getBranchList();
	public Iterator<String> getCommitList();
	public Iterator<CommitFile> getCommitFiles(String commitID);
}
