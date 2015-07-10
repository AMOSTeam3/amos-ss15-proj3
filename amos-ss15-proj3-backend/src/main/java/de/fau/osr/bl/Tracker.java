/*
 * This file is part of ReqTracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * ReqTracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ReqTracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with ReqTracker.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fau.osr.bl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
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
import de.fau.osr.util.NaturalOrderComparator;
import de.fau.osr.util.VisibleFilesTraverser;
import de.fau.osr.util.parser.CommitMessageParser;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.OperationNotSupportedException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutionException;
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
    private VisibleFilesTraverser projectDirTraverser;

    private Logger logger = LoggerFactory.getLogger(Tracker.class);

    private RequirementDao reqDao;
    private CommitDaoImplementation commitDao;

    private LoadingCache<String, List<AnnotatedLine>> blameCache;

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

        blameCache = CacheBuilder.newBuilder()
                .maximumSize(10000)
                .build(
                    new CacheLoader<String, List<AnnotatedLine>>() {
                        public List<AnnotatedLine> load(String path) throws GitAPIException, IOException {
                            GitBlameOperation op = new GitBlameOperation(vcsClient, path, (s, i) -> null);
                            return AnnotatedLine.wordsToLine(op.wordBlame(), dataSource);
                        }
                    }
                );

        this.projectDirTraverser = VisibleFilesTraverser.Get(
                repoFile.getParentFile().toPath(),
                "git",
                ".class"
        );
    }

    /**
     * purges all caches for repository.
     * useful to call if repository is updated by new commit
     */
    public void purgeRepoCache() {
        dataSource.clearCache(); //this will purge probably more than just VCS datasource
        blameCache.cleanUp();
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
        ArrayList<Requirement> reqs = new ArrayList<>(dataSource.getAllRequirements());

        NaturalOrderComparator comparator = new NaturalOrderComparator();

        Collections.sort(reqs, (o1, o2) -> comparator.compare(o1.getId(), o2.getId()));

        return reqs;
    }

    /**
     * This method returns a list of <tt>CommitFile</tt>'s for the given requirement ID.
     */
    @Deprecated
    public List<CommitFile> getCommitFilesForRequirementID(String requirementID) throws IOException {

        long startTime = System.currentTimeMillis();

        logger.info("Start call :: getCommitFilesForRequirementID():requirementID = " + requirementID + " Time:" + startTime);

        List<CommitFile> commitFilesList = new ArrayList<>();

        Set<String> commits = getAllReqCommitRelations().get(requirementID);

        for (String commit : commits){
        	vcsClient.getCommitFiles(commit).get().forEachOrdered(commitFilesList::add);
        }
        
        for(CommitFile file: commitFilesList){
                file.impact = getImpactPercentageForFileAndRequirement(file.newPath.getPath(), requirementID);
        }

        logger.info("End call :: getCommitFilesForRequirementID() Time: " + (System.currentTimeMillis() - startTime));

        return commitFilesList;


    }


    
    public float getImpactPercentageForFileAndRequirement(String file, String requirementID){
        String correctFilePath = file.toString().replaceAll("\\\\", "/");
        List<AnnotatedLine> currentBlame;
        try {
            currentBlame = this.getBlame(correctFilePath);
        } catch (GitAPIException | IOException e) {
            return -1;
        }
        double influenced = 0;
        double currentBlameSize = currentBlame.size();
        for(int i = 0; i<currentBlame.size(); i++){
        	if(currentBlame.get(i).getLine().matches("\\s*")) {
        		// ignore whitespace only lines
        		--currentBlameSize;
        	} else if(currentBlame.get(i).getRequirements().contains(requirementID)){
                influenced += 1./currentBlame.get(i).getRequirements().size();
            }
        }

        float impact = 0;
        if (currentBlameSize != 0 ) {
            impact = (float) ((influenced/currentBlameSize)*100);
        }

        return impact;
    }

    /**
     * This method returns all the requirements for the given File.
     */
    public Set<String> getRequirementIdsForFile(String filePath) throws IOException {
        String correctFilePath = filePath.toString().replaceAll("\\\\", "/");
        
        Set<String> requirementList = new HashSet<>();

        Iterator<String> commitIdListIterator = vcsClient.getCommitListForFileodification(correctFilePath);
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
    @Deprecated
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
            vcsClient.getCommitFiles(currentCommit).get().forEachOrdered(files::add);
        }
        
        return files;
    }

    /**
     * @return Existing project file paths, which are in VCS.
     * @throws IOException
     */
    public Collection<Path> getFiles() throws IOException {
        Collection<Path> filePaths = new ArrayList<>();

        for(Path each: projectDirTraverser.traverse()){
            // fname is a known file in VCS (if it returs ReqIds for that filepath)
            // TODO Maybe we could cache (file->reqIds)
            if (this.getRequirementIdsForFile(each.toString()).size()>0){
                filePaths.add(each);
            }
        }

        return filePaths;
    }

    /**
     * @return Existing project file paths for given requirement
     * @throws IOException
     */
    public Collection<Path> getFilesByRequirement(String requirementId) throws IOException {
        return projectDirTraverser.traverse().stream()
                .filter(path -> {
                    try {
                        return getRequirementIdsForFile(path.toString()).contains(requirementId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    /**
     * @return Existing project file paths for given commit id
     * @throws IOException
     */
    public Collection<Path> getFilesByCommit(String commitId) throws IOException {
        return projectDirTraverser.traverse().stream()
                .filter(path -> {
                    try {
                        return getCommitsByFile(path.toString()).contains(commitId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return false;
                })
                .collect(Collectors.toList());
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
            for(CommitFile commitfile: (Iterable<CommitFile>)(vcsClient.getCommitFiles(currentCommit).get()::iterator)){
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
     * @return collection of commit ids
     */
    public Collection<String> getCommitsByFile(String filePath) throws IOException {
        String correctFilePath = filePath.replaceAll("\\\\", "/");
        return Sets.newHashSet(vcsClient.getCommitListForFileodification(correctFilePath));
    }

    /**
     * get commits that did something with the {@code filePath} file
     * @param filePath file to search for
     * @return collection of commit objects
     */
    public Collection<Commit> getCommitsForFile(String filePath) throws IOException {
        String correctFilePath = filePath.replaceAll("\\\\", "/");
        HashSet<String> ids = Sets.newHashSet(vcsClient.getCommitListForFileodification(correctFilePath));
        return getCommitsByIds(ids);
    }

    /**
     * add Linkage between Requirement and Commit
     * @param commitID and requirementId to be linked
     */
    public void addRequirementCommitRelation(String requirementID, String commitID) throws IOException, OperationNotSupportedException {
        dataSource.addReqCommitRelation(requirementID, commitID);
    }


    public String getCurrentRepositoryPath(){
        return repoFile.toString();
    }

    public List<AnnotatedLine> getBlame(String path) throws IOException, GitAPIException {
        String correctFilePath = path.replaceAll("\\\\", "/");
        try {
            return blameCache.get(correctFilePath);
        } catch (ExecutionException e) {
            throw new IOException(e.getMessage());
        }
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
        String correctFilePath = filePath.replaceAll("\\\\", "/");
    	Collection<AnnotatedLine> lines = this.getBlame(correctFilePath);
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
}