package de.fau.osr.core.vcs.interfaces;

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.RevisionSyntaxException;

import de.fau.osr.core.vcs.base.CommitFile;
import fj.data.Tree;

/**
 * @author Gayathery
 * 
 */
public interface VcsClient {

	public boolean connect();
	public Iterator<String> getBranchList();
	public Iterator<String> getCommitList();
	Tree<String> getCommitTree(String commitID) throws RevisionSyntaxException, AmbiguousObjectException, IncorrectObjectTypeException, IOException;
	public Iterator<CommitFile> getCommitFiles(String commitID);
	public String getCommitMessage(String commitID);
	Iterable<CommitFile> getDiff(String commitA, String commitB);
}
