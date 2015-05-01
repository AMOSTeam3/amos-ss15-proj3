package de.fau.osr.core.vcs.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fau.osr.bl.Tracker;
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
	
	Logger logger = LoggerFactory.getLogger(VcsController.class);

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
		logger.info("Start call Connect():"+repositoryUri);
		switch (sysEnvironment) {
		case GIT:
			if (repositoryUri != null && !repositoryUri.isEmpty()) {
				vcsClient = new GitVcsClient(repositoryUri);
				isConnected=vcsClient.connect();
				
			}
			break;
			
		}
		logger.info("End call Connect():isConnected"+isConnected);
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
	 * @return List of commits for a all modifications in a file
	 * @author Gayathery
	 */
	public Iterator<String> getCommitIdsForFile(String filePath){
		return  isConnected ? vcsClient.getCommitListForFileodification(filePath) : new ArrayList<String>().iterator();
	}
	
	/**
	 * @return List of committed files
	 * @author Gayathery
	 */
	public List<CommitFile> getCommitFiles(String commitID) {
		return isConnected ? vcsClient.getCommitFiles(commitID) : new ArrayList<CommitFile>();

	}

	/**
	 * @return String Message of the requested Commit
	 * @author Florian Gerdes
	 */
	public String getCommitMessage(String commitID) {
		return isConnected ? vcsClient.getCommitMessage(commitID) : null;
	}
}