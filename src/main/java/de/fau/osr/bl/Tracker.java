package de.fau.osr.bl;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Lists;
import de.fau.osr.core.db.CSVFileDataSource;
import de.fau.osr.core.db.DataSource;
import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.interfaces.VcsClient;
import de.fau.osr.util.AppProperties;
import de.fau.osr.util.parser.CommitMessageParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * This class is an interpreter for data from Vcs and Database
 */
public class Tracker {

	VcsClient vcsClient;
	
	DataSource dataSource;

	CommitMessageParser commitMessageparser;
	
	Logger logger = LoggerFactory.getLogger(Tracker.class);

    public Tracker(VcsClient vcsClient) throws IOException {
        this(vcsClient, null);
    }

    public Tracker(VcsClient vcsClient, DataSource ds) throws IOException {

        this.vcsClient = vcsClient;
        commitMessageparser = new CommitMessageParser();
        //assign default value, temp solution, todo
        if (ds == null) {
            ds = new CSVFileDataSource(new File(AppProperties.GetValue("DefaultPathToCSVFile")));
        }

        this.dataSource = ds;

    }

	/* 
	 * @author Gayathery
	 * This method returns a list of FILES for the given requirement ID.
	 */
	public List<CommitFile> getCommitFilesForRequirementID(String requirementID) throws IOException {
		
		long startTime = System.currentTimeMillis();
		
		logger.info("Start call :: getCommitFilesForRequirementID():requirementID = " + requirementID + " Time:" + startTime);

        List<CommitFile> commitFilesList = new ArrayList<>();

        Iterator<String> commits = vcsClient.getCommitList();
        ImmutableSetMultimap<String, String> relationsByCommit = getAllCommitReqRelations();

        while(commits.hasNext()){

            String currentCommit = commits.next();
            if (relationsByCommit.containsKey(currentCommit)){
                commitFilesList.addAll(vcsClient.getCommitFiles(currentCommit));
            }
        }
		

		logger.info("End call :: getCommitFilesForRequirementID() Time: "+ (System.currentTimeMillis() - startTime) );
     			
		return commitFilesList;
		
		
	}
	

	/* (non-Javadoc)
	 * @see de.fau.osr.bl.VcsInterpreter#getCommitFilesForRequirementID(java.lang.String)
	 * @author Gayathery
	 * @desc This method returns all the requirements for the given File.
	 */
	public Set<Integer> getAllRequirementsForFile(String filePath) throws IOException {

        long startTime = System.currentTimeMillis();

        logger.info("Start call : getAllRequirementsForFile():filePath = "+filePath);
        Set<Integer> requirementList = new HashSet<>();

        Iterator<String> commitIdListIterator = vcsClient.getCommitListForFileodification(filePath);
        ImmutableSetMultimap<String, String> relations = getAllCommitReqRelations();

        ImmutableSet<String> allReqs;
        while(commitIdListIterator.hasNext()){
            allReqs = relations.get(commitIdListIterator.next());
            for(String reqId : allReqs){
                requirementList.add(Integer.parseInt(reqId));
            }

        }

        logger.info("End call -getAllRequirementsForFile() Time: "+ (System.currentTimeMillis() - startTime) );
		
		return requirementList;
		
	}
	
    /**
     * relation ReqId - CommitId of VCS + Database
     * @return set of the relations
     * @throws IOException
     */
	public ImmutableSetMultimap<String, String> getAllReqCommitRelations() throws IOException {
        ImmutableSetMultimap.Builder<String, String> totalMap = ImmutableSetMultimap.builder();
        totalMap.putAll(getAllReqCommitRelationsFromVcs());
        totalMap.putAll(dataSource.getAllReqCommitRelations());
        return totalMap.build();
    }

    /**
     * relation CommitId - ReqId of VCS + Database
     * @return set of the relations
     * @throws IOException
     */
    public ImmutableSetMultimap<String, String> getAllCommitReqRelations() throws IOException {
        return getAllReqCommitRelations().inverse();
    }

    /**
     * relation ReqId - CommitId of VCS only
     * @return set of the relations
     */
    protected ImmutableSetMultimap<String, String> getAllReqCommitRelationsFromVcs() {
        ImmutableSetMultimap.Builder<String, String> allRelations = ImmutableSetMultimap.builder();
        ArrayList<String> commitIds = Lists.newArrayList(vcsClient.getCommitList());
        String message;
        List<Integer> reqs;
        for(String commit : commitIds){
            message = vcsClient.getCommitMessage(commit);
            reqs = commitMessageparser.parse(message);
            for(Integer reqId : reqs){
                allRelations.put(reqId.toString(), commit);
            }
        }

        return allRelations.build();
    }
}
