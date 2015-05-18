package de.fau.osr.bl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import de.fau.osr.core.db.CSVFileDataSource;
import de.fau.osr.core.db.CompositeDataSource;
import de.fau.osr.core.db.DataSource;
import de.fau.osr.core.db.VCSDataSource;
import de.fau.osr.core.vcs.base.Commit;
import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.CommitState;
import de.fau.osr.core.vcs.interfaces.VcsClient;
import de.fau.osr.core.vcs.interfaces.VcsClient.AnnotatedLine;
import de.fau.osr.util.AppProperties;
import de.fau.osr.util.parser.CommitMessageParser;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is an interpreter for data from Vcs and Database
 */
public class Tracker {

    File repoFile;

	VcsClient vcsClient;
	
	DataSource dataSource;

	Logger logger = LoggerFactory.getLogger(Tracker.class);

    public Tracker(VcsClient vcsClient) throws IOException {
        this(vcsClient, null, null);
    }

    public Tracker(VcsClient vcsClient, DataSource ds, File repoFile) throws IOException {
        this.repoFile = repoFile;
        this.vcsClient = vcsClient;

        //assign default value, temp solution, todo
        if (repoFile == null) repoFile = new File(AppProperties.GetValue("DefaultRepoPath"));
        if (ds == null) {
            CSVFileDataSource csvDs = new CSVFileDataSource(new File(repoFile.getParentFile(), AppProperties.GetValue("DefaultPathToCSVFile")));

            CommitMessageParser parser = new CommitMessageParser(Pattern.compile(AppProperties.GetValue("RequirementPattern")));
            VCSDataSource vcsDs = new VCSDataSource(vcsClient, parser);

            ds = new CompositeDataSource(csvDs, vcsDs);
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

        Set<String> commits = getAllReqCommitRelations().get(requirementID);

        for (String commit : commits){
            commitFilesList.addAll(vcsClient.getCommitFiles(commit));
        }
        
        for(CommitFile file: commitFilesList){
        	List<AnnotatedLine> currentBlame;
			try {
				currentBlame = this.getBlame(file.newPath.getPath().toString());
			} catch (GitAPIException | IOException e) {
				continue;
			}
			int i = 1;
			int influenced = 0;
			for(; i<currentBlame.size(); i++){
				if(currentBlame.get(i).getRequirements().contains(requirementID)){
					influenced++;
				}
			}
			file.impact = (influenced/i)*100;
		}

		logger.info("End call :: getCommitFilesForRequirementID() Time: "+ (System.currentTimeMillis() - startTime) );
     			
		return commitFilesList;
		
		
	}
	
	public float getImpactPercentageForFileAndRequirement(String file, String requirementID){
		List<AnnotatedLine> currentBlame;
		try {
			currentBlame = this.getBlame(file);
		} catch (GitAPIException | IOException e) {
			return -1;
		}
		int i = 1;
		int influenced = 0;
		for(; i<currentBlame.size(); i++){
			if(currentBlame.get(i).getRequirements().contains(requirementID)){
				influenced++;
			}
		}
		float impact = (influenced/i)*100;
		return impact;
	}
	

	/* (non-Javadoc)
	 * @see de.fau.osr.bl.VcsInterpreter#getCommitFilesForRequirementID(java.lang.String)
	 * @author Gayathery
	 * @desc This method returns all the requirements for the given File.
	 */
	public Set<String> getAllRequirementsForFile(String filePath) throws IOException {

        long startTime = System.currentTimeMillis();

        logger.debug("Start call : getAllRequirementsForFile():filePath = "+filePath);
        Set<String> requirementList = new HashSet<>();

        Iterator<String> commitIdListIterator = vcsClient.getCommitListForFileodification(filePath);
        SetMultimap<String, String> relations = getAllCommitReqRelations();

        while(commitIdListIterator.hasNext()){
                requirementList.addAll(relations.get(commitIdListIterator.next()));
        }

        logger.debug("End call -getAllRequirementsForFile() Time: "+ (System.currentTimeMillis() - startTime) );
		
		return requirementList;
		
	}
	
    /**
     * relation ReqId - CommitId of VCS + Database
     * @return set of the relations
     * @throws IOException
     */
	public SetMultimap<String, String> getAllReqCommitRelations() throws IOException {
        return dataSource.getAllReqCommitRelations();
    }

    /**
     * relation CommitId - ReqId of VCS + Database
     * @return set of the relations
     * @throws IOException
     */
    public SetMultimap<String, String> getAllCommitReqRelations() throws IOException {
        SetMultimap<String, String> result = HashMultimap.create();
        Multimaps.invertFrom(getAllReqCommitRelations(), result);
        return result;
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
    public Collection<CommitFile> getAllFiles(){
        Set<CommitFile> files = new HashSet<CommitFile>();
        Iterator<String> allCommits = vcsClient.getCommitList();
        while (allCommits.hasNext()) {
            String currentCommit = allCommits.next();
            for(CommitFile commitfile: vcsClient.getCommitFiles(currentCommit)){
                files.add(commitfile);
            }
        }
        
        return files;
    }
    
    /**
     * @return all ever committed file paths
     */
    public Collection<String> getAllFilesAsString(){
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
    public Collection<String> getRequirementsFromCommit(Commit commit) throws IOException {
        return dataSource.getReqRelationByCommit(commit.id);
    }
	
    /**
     * add Linkage between Requirement and Commit
     * @param commitID and requirementId to be linked
     * @return 
     */
	public void addRequirementCommitRelation(String requirementID,
			String commitID) throws Exception {
		dataSource.addReqCommitRelation(requirementID, commitID);
	}


    public String getCurrentRepositoryPath(){
        return repoFile.toString();
    }
    
    public List<AnnotatedLine> getBlame(String path) throws IOException, GitAPIException {
    	return vcsClient.blame(path,  dataSource);
    }
 /*
  * Method which performs the complete processing of Requirement Traceability
  */
    public RequirementsTraceabilityMatrix generateRequirementsTraceability() throws IOException{
    	
    	try{
	    	
	    	
	    	Collection<String> files = getAllFilesAsString() ;	    	
	    	
	    	ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(AppProperties.GetValueAsInt("TraceabilityMatrixProcessingThreadPoolCount"));
	    	TraceabilityMatrixThread.setRequirementTraceabilityMatrix(this);
	    	for(String file: files){	    		
	    		TraceabilityMatrixThread traceabilityMatrixWorkerThread = new TraceabilityMatrixThread(file);
	    		threadPoolExecutor.execute(traceabilityMatrixWorkerThread);
	    	}
	    	threadPoolExecutor.shutdown();
	    	while (!threadPoolExecutor.isTerminated()) {
	    		        }
	    	return TraceabilityMatrixThread.getRequirementTraceabilityMatrix();
	    	
    	}catch(IndexOutOfBoundsException e){
    		return null;
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	return null;
    	
    }
    /*
     * Method which performs the complete processing of Requirement Traceability by Impact
     */
    public RequirementsTraceabilityMatrixByImpact generateRequirementsTraceabilityByImpact(){
    	RequirementsTraceabilityMatrixByImpact requirementsTraceabilityMatrixByImpact = new RequirementsTraceabilityMatrixByImpact(this);
    	requirementsTraceabilityMatrixByImpact.Process();
    	return requirementsTraceabilityMatrixByImpact;
    }
}
/*
 * class for Thread of Traceability Matrix processing
 */
class TraceabilityMatrixThread implements Runnable{
	static Tracker tracker;
	String filePath;
	static RequirementsTraceabilityMatrix requirementsTraceabilityMatrixWorker;
	static void setRequirementTraceabilityMatrix(Tracker trackerObject){
		try {
			tracker = trackerObject;
			requirementsTraceabilityMatrixWorker = new RequirementsTraceabilityMatrix(tracker.getAllRequirements());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static RequirementsTraceabilityMatrix getRequirementTraceabilityMatrix(){
		return requirementsTraceabilityMatrixWorker;
	}
	TraceabilityMatrixThread(String filePath){
		this.filePath = filePath;
	}

	@Override
	public void run() throws IndexOutOfBoundsException{
		List<String> fileRequirements;
		try {
			String unixFormatedFilePath = filePath.replaceAll(Matcher.quoteReplacement("\\"), "/");
			fileRequirements = new ArrayList<String>( tracker.getAllRequirementsForFile(unixFormatedFilePath));
			if(fileRequirements !=null && !fileRequirements.isEmpty())
    			requirementsTraceabilityMatrixWorker.populateMatrix(fileRequirements,unixFormatedFilePath);
		} catch(IndexOutOfBoundsException e){
    		throw e;
    	}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}


