package de.fau.osr.bl;

import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Lists;
import de.fau.osr.core.db.CSVFileDataSource;
import de.fau.osr.core.db.DataSource;
import de.fau.osr.core.vcs.base.Commit;
import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.CommitState;
import de.fau.osr.core.vcs.interfaces.VcsClient;
import de.fau.osr.util.AppProperties;
import de.fau.osr.util.parser.CommitMessageParser;
import de.fau.osr.util.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is an interpreter for data from Vcs and Database
 */
public class Tracker {

    File repoFile;

	VcsClient vcsClient;
	
	DataSource dataSource;

	CommitMessageParser commitMessageparser;
	
	Logger logger = LoggerFactory.getLogger(Tracker.class);

    public Tracker(VcsClient vcsClient) throws IOException {
        this(vcsClient, null, null, null);
    }

    public Tracker(VcsClient vcsClient, DataSource ds, File repoFile, String parsePattern) throws IOException {
        this.repoFile = repoFile;
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
	public Set<String> getAllRequirementsForFile(String filePath) throws IOException {

        long startTime = System.currentTimeMillis();

        logger.info("Start call : getAllRequirementsForFile():filePath = "+filePath);
        Set<String> requirementList = new HashSet<>();

        Iterator<String> commitIdListIterator = vcsClient.getCommitListForFileodification(filePath);
        ImmutableSetMultimap<String, String> relations = getAllCommitReqRelations();

        while(commitIdListIterator.hasNext()){
                requirementList.addAll(relations.get(commitIdListIterator.next()));
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
        List<String> reqs;
        for(String commit : commitIds){
            message = vcsClient.getCommitMessage(commit);
            reqs = commitMessageparser.parse(message);
            for(String reqId : reqs){
                allRelations.put(reqId, commit);
            }
        }

        return allRelations.build();
    }

    public Collection<Commit> getCommitsForRequirementID(String requirementID) throws IOException {
        ArrayList<Commit> commits = new ArrayList<Commit>();

        for(String commitID: getAllReqCommitRelations().get(requirementID)){
            commits.add(new Commit(commitID, vcsClient.getCommitMessage(commitID), null, vcsClient.getCommitFiles(commitID)));
        }

        return commits;
    }

    /**
     * @return list of all requirement ids
     */
    public Collection<String> getAllRequirements() throws IOException {
        return getAllReqCommitRelations().keySet();
    }


    /**
     * @return all ever committed file paths
     */
    public Collection<String> getAllFiles(){
        Set<String> files = new HashSet<String>();
        Iterator<String> allCommits = vcsClient.getCommitList();
        while (allCommits.hasNext()) {
            String currentCommit = allCommits.next();
            for(CommitFile commitfile: vcsClient.getCommitFiles(currentCommit)){
                String name;
                if(commitfile.commitState == CommitState.DELETED){
                    name = commitfile.oldPath.getPath();
                }else{
                    name = commitfile.newPath.getPath();
                }
                Pattern pattern = Pattern.compile("src");
                Matcher m = pattern.matcher(name);
                if(m.find()){
                    files.add(name);
                }
            }
        }
        return files;
    }

    /**
     * @return collection of all commit objects
     */
    public Collection<Commit> getCommits() {
        Iterator<String> iterator = vcsClient.getCommitList();
        ArrayList<Commit> commits = new ArrayList<Commit>();
        while(iterator.hasNext()){
            String Id = iterator.next();
            commits.add(new Commit(Id, vcsClient.getCommitMessage(Id), null, vcsClient.getCommitFiles(Id)));

        }
        return commits;
    }

    /**
     * get commits that did something with the {@code filePath} file
     * @param filePath file to search for
     * @return collection of commit objects
     */
    public Collection<Commit> getCommitsFromFile(String filePath) {
        Iterator<String> iterator = vcsClient.getCommitListForFileodification(filePath);
        ArrayList<Commit> commits = new ArrayList<Commit>();
        while(iterator.hasNext()){
            String Id = iterator.next();
            commits.add(new Commit(Id, vcsClient.getCommitMessage(Id), null, vcsClient.getCommitFiles(Id)));

        }
        return commits;
    }

    /**
     * parses requirements from commit
     * @param commit commit to read from
     * @return collection of requirement ids
     * todo move to vcsClient?
     */
    public Collection<String> getRequirementsFromCommit(Commit commit) {
        Parser parser = new CommitMessageParser();
        return parser.parse(commit.message);
    }

    /**
     * method to get the current requirement pattern
     * @return
     */
    public String getCurrentRequirementPatternString(){
        return CommitMessageParser.getPattern().toString();
    }

    public String getCurrentRepositoryPath(){
        return repoFile.toString();
    }
}
