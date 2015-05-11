package de.fau.osr.core.vcs.interfaces;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jgit.errors.RepositoryNotFoundException;

import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.VcsEnvironment;
import de.fau.osr.core.vcs.impl.GitVcsClient;

/**
 * @author Gayathery
 * 
 */
public abstract class VcsClient {

	public abstract Iterator<String> getBranchList();
	public abstract Iterator<String> getCommitList();
	public abstract ArrayList<CommitFile> getCommitFiles(String commitID);
	public abstract String getCommitMessage(String commitID);
	public abstract Iterator<String> getCommitListForFileodification(String filePath);
	public static VcsClient connect(VcsEnvironment env, String repositoryURI) {
		VcsClient client;
		try {
			switch(env) {
			case GIT:
				client = new GitVcsClient(repositoryURI);
				break;
			default:
				throw new RuntimeException("unknown vcs environment " + env);
			}
		} catch(RepositoryNotFoundException e){
			throw new RuntimeException("Repository Not Found");
		}catch(IOException e) {
			throw new RuntimeException(e);
		}
		
		return client;
	}
}
