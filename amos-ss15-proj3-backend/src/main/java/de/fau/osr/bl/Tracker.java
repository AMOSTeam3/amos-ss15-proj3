package de.fau.osr.bl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import de.fau.osr.core.Requirement;
import de.fau.osr.core.db.CSVFileDataSource;
import de.fau.osr.core.db.CompositeDataSource;
import de.fau.osr.core.db.DataSource;
import de.fau.osr.core.db.VCSDataSource;
import de.fau.osr.core.db.dao.RequirementDao;
import de.fau.osr.core.db.dao.impl.CommitDaoImplementation;
import de.fau.osr.core.db.dao.impl.RequirementDaoImplementation;
import de.fau.osr.core.vcs.AnnotatedLine;
import de.fau.osr.core.vcs.base.Commit;
import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.CommitState;
import de.fau.osr.core.vcs.impl.GitBlameOperation;
import de.fau.osr.core.vcs.interfaces.VcsClient;
import de.fau.osr.util.AppProperties;
import de.fau.osr.util.parser.CommitMessageParser;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This class is an interpreter for data from Vcs and Database
 */
public class Tracker {

    private File repoFile;
    private VcsClient vcsClient;

    private DataSource dataSource;

    private Logger logger = LoggerFactory.getLogger(Tracker.class);

    private RequirementDao reqDao;
    private CommitDaoImplementation commitDao;

    public Tracker(VcsClient vcsClient) throws IOException {
        this(vcsClient, null, null);
    }

    public Tracker(VcsClient vcsClient, DataSource ds, File repoFile) throws IOException {
        init(vcsClient, ds, repoFile);

        this.reqDao = new RequirementDaoImplementation();
        this.commitDao = new CommitDaoImplementation();

    }

    public Tracker(VcsClient vcsClient, DataSource ds, File repoFile, SessionFactory sessionFactory) throws IOException {
        init(vcsClient, ds, repoFile);
        this.reqDao = new RequirementDaoImplementation(sessionFactory);
        this.commitDao = new CommitDaoImplementation(sessionFactory);

    }

    /**
     * private method to initialize the tracker, uses default values from properties, if parameters are null
     * @throws IOException
     */
    private void init(VcsClient vcsClient, DataSource ds, File repoFile) throws IOException {
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

    /**
     * @return collection of all commit objects
     */
    public Collection<Commit> getCommits() throws IOException {
        HashSet<String> ids = Sets.newHashSet(vcsClient.getCommitList());
        return getCommitsByIds(ids);
    }

    /**
     * @return all requirement objects, if possible filled with data from database
     * @throws IOException
     */
    public Collection<Requirement> getRequirements() throws IOException {
        return dataSource.getAllRequirements();
    }

    /**
     * This method returns a list of <tt>CommitFile</tt>'s for the given requirement ID.
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
                currentBlame = this.getBlame(file.newPath.getPath());
            } catch (GitAPIException | IOException e) {
                continue;
            }
            int i = 0;
            float influenced = 0;
            int currentBlameSize = currentBlame.size();
            for(; i<currentBlameSize; i++){
                if(currentBlame.get(i).getRequirements().contains(requirementID)){
                    influenced++;
                }
            }

            if (currentBlameSize != 0 ) {
                file.impact = (influenced/currentBlameSize)*100;
            }
        }

        logger.info("End call :: getCommitFilesForRequirementID() Time: " + (System.currentTimeMillis() - startTime));

        return commitFilesList;


    }


    
    public float getImpactPercentageForFileAndRequirement(String file, String requirementID){
        List<AnnotatedLine> currentBlame;
        try {
            currentBlame = this.getBlame(file);
        } catch (GitAPIException | IOException e) {
            return -1;
        }
        int i = 0;
        float influenced = 0;
        int cuurentBlameSize = currentBlame.size();
        for(; i<cuurentBlameSize; i++){
            if(currentBlame.get(i).getRequirements().contains(requirementID)){
                influenced++;
            }
        }

        float impact = 0;
        if (cuurentBlameSize != 0) {
            impact = (influenced/cuurentBlameSize)*100;
        }

        return impact;
    }

    /**
     * This method returns all the requirements for the given File.
     */
    public Set<String> getRequirementIdsForFile(String filePath) throws IOException {

        Set<String> requirementList = new HashSet<>();

        Iterator<String> commitIdListIterator = vcsClient.getCommitListForFileodification(filePath);
        SetMultimap<String, String> relations = getAllCommitReqRelations();

        while(commitIdListIterator.hasNext()){
            requirementList.addAll(relations.get(commitIdListIterator.next()));
        }

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

    /**
     * @return all commit objects, are related to this requirement id
     * @throws IOException
     */
    public Set<Commit> getCommitsForRequirementID(String requirementID) throws IOException {
        Set<Commit> commits = new HashSet<>();

        for(String commitID: getAllReqCommitRelations().get(requirementID)){
            commits.add(new Commit(commitID,
                    vcsClient.getCommitMessage(commitID),
                    dataSource.getCommitRelationByReq(commitID),
                    vcsClient.getCommitFiles(commitID)));
        }

        return commits;
    }

    /**
     * @return list of all requirement ids
     */
    public Collection<String> getRequirementIds() throws IOException {

        Set<Requirement> allReqs = dataSource.getAllRequirements();
        HashSet<String> result = new HashSet<>();
        for (Requirement req : allReqs) {
            result.add(req.getId());
        }

        return result;
    }


    /**
     * @return all ever committed files, as <tt>CommitFiles</tt>
     */
    public Collection<CommitFile> getAllCommitFiles(){
        Set<CommitFile> files = new HashSet<>();
        Iterator<String> allCommits = vcsClient.getCommitList();
        while (allCommits.hasNext()) {
            String currentCommit = allCommits.next();
            files.addAll(vcsClient.getCommitFiles(currentCommit));
        }
        
        return files;
    }

    /**
     * @return all existing ever committed file paths
     */
    public Collection<String> getAllFilesAsString(){
        Set<String> files = new HashSet<String>();
        Set<String> deletedFiles = new HashSet<String>();
        Iterator<String> allCommits = vcsClient.getCommitList();
        while (allCommits.hasNext()) {
            String currentCommit = allCommits.next();
            for(CommitFile commitfile: vcsClient.getCommitFiles(currentCommit)){
                if(!(commitfile.commitState == CommitState.DELETED))
                    files.add(commitfile.newPath.getPath());
                else 
                   deletedFiles.add(commitfile.oldPath.getPath());
              
            }
        }
        files.removeAll(deletedFiles);
        return files;
    }


    /**
     * get commits that did something with the {@code filePath} file
     * @param filePath file to search for
     * @return collection of commit objects
     */
    public Collection<Commit> getCommitsForFile(String filePath) throws IOException {
        HashSet<String> ids = Sets.newHashSet(vcsClient.getCommitListForFileodification(filePath));
        return getCommitsByIds(ids);
    }

    /**
     * add Linkage between Requirement and Commit
     * @param commitID and requirementId to be linked
     */
    public void addRequirementCommitRelation(String requirementID, String commitID) throws Exception {
        dataSource.addReqCommitRelation(requirementID, commitID);
    }


    public String getCurrentRepositoryPath(){
        return repoFile.toString();
    }

    public List<AnnotatedLine> getBlame(String path) throws IOException, GitAPIException {
        GitBlameOperation op = new GitBlameOperation(vcsClient, path, (s, i) -> null);
        return AnnotatedLine.wordsToLine(op.wordBlame(), dataSource);
    }

    /**
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

    /**
     * Method which performs the complete processing of Requirement Traceability by Impact
     */
    public RequirementsTraceabilityMatrixByImpact generateRequirementsTraceabilityByImpact(){
        RequirementsTraceabilityMatrixByImpact requirementsTraceabilityMatrixByImpact = new RequirementsTraceabilityMatrixByImpact(this);
        requirementsTraceabilityMatrixByImpact.Process();
        return requirementsTraceabilityMatrixByImpact;
    }

    /**
     * @return current repository name from VCS
     */
    public String getRepositoryName(){
        return vcsClient.getRepositoryName();
    }
    
    /**
     * @param filePath
     * @return one Collection per line in the file, of formatted requirements
     * that are impacted by that line  
     * @throws Exception
     */
    public List<Collection<String>> getRequirementsLineLinkageForFile(String filePath) throws IOException, GitAPIException {
    	Collection<AnnotatedLine> lines = this.getBlame(filePath);
    	List<Collection<String>> reqIdsByLines = new ArrayList<>();

        //preload all reqs to ID -> Req IdentityHashMap
        Collection<Requirement> allReqs = getRequirements();
        IdentityHashMap<String, Requirement> reqsById = new IdentityHashMap<>();
        for (Requirement req : allReqs) {
            reqsById.put(req.getId(), req);
        }

        for(AnnotatedLine line: lines){
    		final Collection<String> requirements = line.getRequirements();
    		Collection<String> annotation = new ArrayList<>();
    		reqIdsByLines.add(annotation);
    		for(String reqId : requirements) {
    			//fetch the req data from data source
    			Requirement req = reqsById.get(reqId);

    			if(req != null)
    				annotation.add("Req " + reqId + ": \"" + req.getTitle() + "\" " + req.getDescription());
    			else // req is not in the database
    				annotation.add("Req " + reqId);
    		}
    	}

    	return reqIdsByLines;
    }


    /**
     * @param id id of requirement
     * @return data object requirement by id
     */
    public Requirement getRequirementById(String id) throws IOException {
        Collection<Requirement> allReqs = getRequirements();
        for (Requirement req : allReqs) {
            if (req.getId().equals(id)){
                return req;
            }
        }

        throw new NoSuchElementException("Requirement with id " + id + " not found");
    }

    /**
     * create collection of known Requirement objects, by given set of requirement IDs
     * @param reqIds set of req ids
     * @return Collection of Requirement domain objects
     * @throws IOException
     */
    public Collection<Requirement> getRequirementsByIds(Set<String> reqIds) throws IOException {
        final Set<String> finalReqs = reqIds;

        Collection<Requirement> reqObjects = getRequirements();

        return reqObjects.stream()
                .filter(req -> finalReqs.contains(req.getId()))
                .collect(Collectors.toList());
    }

    /**
     * creates/gets commit objects, by given ids.
     * @return set of commit objects
     * @throws IOException
     */
    public Set<Commit> getCommitsByIds(Collection<String> commitIds) throws IOException {
        Set<Commit> commits = new HashSet<>();
        SetMultimap<String, String> relations = getAllCommitReqRelations();
        for (String id : commitIds) {
            commits.add(new Commit(id, vcsClient.getCommitMessage(id), relations.get(id), vcsClient.getCommitFiles(id)));
        }

        return commits;
    }

    /**
     * creates or updates requirement in database
     * @param id id of req to update or create
     * @param title new title
     * @param description new description
     */
    public void saveOrUpdateRequirement(String id, String title, String description) throws IOException, OperationNotSupportedException {
        dataSource.saveOrUpdateRequirement(id, title, description);
    }


    /*--- deprecated, remove later ---*/

    /**
     * get commits that did something with the {@code filePath} file
     * @param filePath file to search for
     * @return collection of commit objects
     * @deprecated use getCommitsForFile instead
     */
    public Collection<Commit> getCommitsFromFile(String filePath) throws IOException {
        return getCommitsForFile(filePath);
    }

    /**
     * @deprecated use getCommitsByIds()
     */
    public Set<Commit> getCommitObjectsByIds(Collection<String> commitIds) throws IOException {
        return getCommitsByIds(commitIds);
    }

    /**
     * @deprecated use getRequirementsByIds()
     */
    public Collection<Requirement> getReqObjectsByIds(Set<String> reqIds) throws IOException {
        return getRequirementsByIds(reqIds);
    }

    /**
     * @return all known domain commits objects
     * @deprecated use getCommits()
     */
    public Set<Commit> getAllCommitObjects() throws IOException {
        return new HashSet<>(getCommits());
    }

    /**
     * @deprecated use getRequirementById()
     */
    public Requirement getRequirementObjectById(String id) throws IOException {
        return getRequirementById(id);
    }

    /**
     * @deprecated use getRequirements()
     */
    public Collection<Requirement> getAllRequirementObjects() throws IOException {
        return getRequirements();
    }

    /**
     * @deprecated use getRequirementIds()
     */
    public Collection<String> getAllRequirements() throws IOException {
        return getRequirementIds();
    }

    /**
     * @deprecated use getRequirementIdsForFile()
     */
    public Set<String> getAllRequirementsForFile(String filePath) throws IOException {
        return getRequirementIdsForFile(filePath);
    }

    /**
     * @deprecated use getAllCommitFiles()
     */
    public Collection<CommitFile> getAllFiles(){
        return getAllCommitFiles();
    }
}




