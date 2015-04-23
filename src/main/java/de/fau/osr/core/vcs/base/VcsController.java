package de.fau.osr.core.vcs.base;

import java.util.ArrayList;
import java.util.Iterator;

import de.fau.osr.core.vcs.impl.GitVcsClient;
import de.fau.osr.core.vcs.interfaces.VcsClient;

/**
 * @author Gayathery
 * @desc This class is a client to the VersionControl. This class exposes methods 
 * to connect to the supported instance of the vcs and perform basic operations like 
 * getbrach details, get commit details....
 *
 */

public class VcsController {

	VcsEnvironment sysEnvironment;
	VcsClient vcsClient;
	Boolean isConnected;

	/**
	 * @param env
	 * @author Gayathery
	 */
	public VcsController(VcsEnvironment env) {
		sysEnvironment = env;
		isConnected = false;
	}

	/**
	 * @author Gayathery
	 */
	private VcsController() {
	}

	
	/**
	 * @author Gayathery
	 */
	public void Connect() {
		Connect(null);
	}
    
	/**
	 * @param repositoryUri
	 * @author Gayathery
	 */
	public boolean Connect(String repositoryUri) {
		switch (sysEnvironment) {
		case GIT:
			if (repositoryUri != null && !repositoryUri.isEmpty()) {
				vcsClient = new GitVcsClient(repositoryUri);
				isConnected=vcsClient.connect();
				
			}
			break;
			
		}
		return isConnected;
	}

	
	/**
	 * @return List of branches
	 * @author Gayathery
	 */
	public Iterator<String> getBranchList() {
		return isConnected ? vcsClient.getBranchList():new ArrayList<String>().iterator();
	}

	/**
	 * @return List of commits
	 * @author Gayathery
	 */
	public Iterator<String> getCommitList() {
		return  isConnected ? vcsClient.getCommitList() : new ArrayList<String>().iterator();
	}
	
	/**
	 * @return List of committed files
	 * @author Gayathery
	 */
	public Iterator<CommitFile> getCommitFiles(String commitID) {
		return isConnected ? vcsClient.getCommitFiles(commitID) : new ArrayList<CommitFile>().iterator();

	}
	

	/**
	 * @return String Message of the requested Commit
	 * @author Florian Gerdes
	 */
	public String getCommitMessage(String commitID) {
		return isConnected ? vcsClient.getCommitMessage(commitID) : null;
	}
}