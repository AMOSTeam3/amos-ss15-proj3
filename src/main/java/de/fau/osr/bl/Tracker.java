package de.fau.osr.bl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.fau.osr.util.parser.CommitMessageParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.VcsController;
/**
 * @author Gayathery
 * @desc This class is an interpreter for data from Vcs
 *
 */
public class Tracker {

	VcsController vcsController;
	
	CommitMessageParser commitMessageparser;
	
	Logger logger = LoggerFactory.getLogger(Tracker.class);

	public Tracker(VcsController vcsController) {
		
		this.vcsController = vcsController;
	}

	/* 
	 * @author Gayathery
	 * This method returns a list of FILES for the given requirement ID.
	 */
	
	public List<CommitFile> getCommitFilesForRequirementID(String requirementID){
		
		long startTime = System.currentTimeMillis();
		
		logger.info("Start call :: getCommitFilesForRequirementID():requirementID ="+requirementID + " Time:"+startTime);
		
		List<CommitFile> commitFilesList = new ArrayList<CommitFile>();
		
		commitFilesList.addAll(getFilesByRequirementID(requirementID));
		
		commitFilesList.addAll(getFilesByLinkage(requirementID));
		
		logger.info("End call :: getCommitFilesForRequirementID() Time: "+ (System.currentTimeMillis() - startTime) );
     			
		return commitFilesList;
		
		
	}
	
	
	
	/**
	 * @Gayathery
	 * @desc This method returns all the commit files for the given requirement Id from the source control repository
	 * @param requirementID
	 * @return
	 */
	private List<CommitFile> getFilesByRequirementID(String requirementID){
		
		long startTime = System.currentTimeMillis();
		
		logger.info("Start call :: getFilesByRequirementID():requirementID ="+requirementID + " Time:"+startTime);
		
		List<CommitFile> commitFilesList = new ArrayList<CommitFile>();
		
     	Iterator<String> commits = vcsController.getCommitList();
	
     	while(commits.hasNext()){
     		
     		String currentCommit = commits.next();
			
     		commitMessageparser = new CommitMessageParser();
     		
     		if(commitMessageparser.parse(vcsController.getCommitMessage(currentCommit)).contains(Integer.valueOf(requirementID)))
     			
     			commitFilesList.addAll(vcsController.getCommitFiles(currentCommit));
					
		}
		
     	logger.info("End call :: getFilesByRequirementID() Time: "+ (System.currentTimeMillis() - startTime) );
     	
		return commitFilesList;
	}
	
	/**
	 * @Gayathery
	 * @desc This method returns all the commit files for the requirement ID which are additionally linked to commits.
	 * @param requirementID
	 * @return
	 */
	private List<CommitFile> getFilesByLinkage(String requirementID){
		
		long startTime = System.currentTimeMillis();
		
		logger.info("Start call :: getFilesByLinkage():requirementID ="+requirementID + " Time:"+startTime);
		
		logger.info("End call :: getFilesByLinkage() Time: "+ (System.currentTimeMillis() - startTime) );
		
		//TODO: add the method call to get linkage from database
		
		List<CommitFile> commitFilesList = new ArrayList<CommitFile>();
     	
		return commitFilesList;
		
	}
	
	/* (non-Javadoc)
	 * @see de.fau.osr.bl.VcsInterpreter#getCommitFilesForRequirementID(java.lang.String)
	 * @author Gayathery
	 * @desc This method returns all the requirements for the given File.
	 */
	
	public List<Integer> getAllRequirementsforFile(String filePath){
		
		long startTime = System.currentTimeMillis();
		
		logger.info("Start call : getAllRequirementsforFile():filePath ="+filePath);
		
		List<Integer> requirementList = new ArrayList<Integer>();
		
		requirementList.addAll(getRequirementsListByFile(filePath));
		
		requirementList.addAll(getRequirementsListByLinkage(filePath));
		
		logger.info("End call -getAllRequirementsforFile() Time: "+ (System.currentTimeMillis() - startTime) );
		
		return requirementList;
		
	}
	
	private Set<Integer> getRequirementsListByFile(String filePath){
		
		long startTime = System.currentTimeMillis();
		
		logger.info("Start call : getRequirementsListByFile():filePath ="+filePath);
		
		Set<Integer> requirementList = new HashSet<Integer>();
		
		Iterator<String> commitIdListIterator = vcsController.getCommitIdsForFile(filePath);
		
		commitMessageparser = new CommitMessageParser();
		
		while(commitIdListIterator.hasNext()){
			
			requirementList.addAll(commitMessageparser.parse(vcsController.getCommitMessage(commitIdListIterator.next())));
		}
		
		logger.info("End call -getRequirementsListByFile() Time: "+ (System.currentTimeMillis() - startTime) );
		
		return requirementList;
	}
	
	
	/**
	 * @Gayathery
	 * @desc This method return the requirement Ids for the given File , due to explicit linkages from DB 
	 * @param filePath
	 * @return
	 */
	private Set<Integer> getRequirementsListByLinkage(String filePath){
		
		long startTime = System.currentTimeMillis();
		
		logger.info("Start call :: getRequirementsListByLinkage():filePath ="+filePath);
		
		Set<Integer> requirementList = new HashSet<Integer>();
		
		//TODO:add the method call to actually return the linkage from DB
		
		logger.info("End call :: getRequirementsListByLinkage() Time: "+ (System.currentTimeMillis() - startTime) );
		
		return requirementList;
	}
}
