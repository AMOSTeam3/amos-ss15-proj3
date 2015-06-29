package de.fau.osr.gui.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

import javax.naming.OperationNotSupportedException;

import org.eclipse.jgit.api.errors.GitAPIException;

import de.fau.osr.bl.RequirementsTraceabilityMatrix;
import de.fau.osr.bl.RequirementsTraceabilityMatrixByImpact;
import de.fau.osr.bl.Tracker;
import de.fau.osr.gui.Model.DataElements.AnnotatedLine;
import de.fau.osr.gui.Model.DataElements.Commit;
import de.fau.osr.gui.Model.DataElements.CommitFile;
import de.fau.osr.gui.Model.DataElements.Requirement;
import de.fau.osr.gui.util.ElementsConverter;

/**
 * @author Gayathery
 * This class 'TrackerAdapterWorker' works as an indexer for the application.
 * This class collects data in the background and when it is ready it masks the Tracker.
 * When there is a change in the underlying data (example: new commit) then it automatically stops masking until it
 * refreshes its data repository. Time consuming methods can be masked using this class
 */
public class TrackerAdapterWorker {

    private TrackerAdapter trackerAdapter;
    Boolean isReadyForTakeOver;
    private Pattern reqPatternString;
    public Collection<Requirement> requirements;
    private Collection<CommitFile> totalCommitFiles;
    private HashMap<Requirement,Collection<Commit>> workerRepositoryReqCommit;
    private HashMap<Commit,Collection<CommitFile>> workerRepositoryCommitCommitFile;
    private HashMap<Requirement,Collection<CommitFile>> workerRepositoryReqCommitFile;
    public static Date globalChangeTime;
    private static long CommitCount;
    public static Date getAllRequirements,getImpactPercentageForCommitFileListAndRequirement,getCommitsFromRequirement,getAllFiles,getRequirementsFromFile,getCommitFilesForRequirement;
    public Boolean isThreadingRequired;
    public TrackerAdapterWorker(TrackerAdapter trackerAdapter) throws IOException {
        this.trackerAdapter = trackerAdapter;
        isReadyForTakeOver = false;
        Calendar calc = Calendar.getInstance();
        globalChangeTime = calc.getTime();
        workerRepositoryReqCommit = new HashMap<Requirement, Collection<Commit>>();
        workerRepositoryCommitCommitFile = new HashMap<Commit, Collection<CommitFile>>();
        workerRepositoryReqCommitFile = new HashMap<Requirement, Collection<CommitFile>>();
        totalCommitFiles = new ArrayList<CommitFile>();
        isThreadingRequired = false;
        
    }
    
    public void setActualTrackerAdapter(TrackerAdapter trackerAdapter){
        this.trackerAdapter = trackerAdapter;
    }
    public void setThreading(Boolean isRequired){
        this.isThreadingRequired = isRequired;
    }
    public Date trackerLastChangeTime(){
        
        return globalChangeTime;
    }
    
    class Collector{
        
        public Collection<CommitFile> totalCommitFiles;
        public HashMap<Requirement,Collection<Commit>> workerRepositoryReqCommit;
        public HashMap<Commit,Collection<CommitFile>> workerRepositoryCommitCommitFile;
        public HashMap<Requirement,Collection<CommitFile>> workerRepositoryReqCommitFile;
        
        public Collector(){
            
            totalCommitFiles = new ArrayList<CommitFile>();
            workerRepositoryReqCommit = new HashMap<Requirement, Collection<Commit>>();
            workerRepositoryCommitCommitFile = new HashMap<Commit, Collection<CommitFile>>();
            workerRepositoryReqCommitFile = new HashMap<Requirement, Collection<CommitFile>>();
            
        }
    }
    
    public void collectFutures(Future<Collector> future){
        try {
            Collector collected = future.get();
            
            totalCommitFiles.addAll(collected.totalCommitFiles);
           workerRepositoryReqCommit.putAll(collected.workerRepositoryReqCommit);
            workerRepositoryCommitCommitFile.putAll(collected.workerRepositoryCommitCommitFile);
             workerRepositoryReqCommitFile.putAll(collected.workerRepositoryReqCommitFile);
        } catch (InterruptedException e) {
            
            e.printStackTrace();
            return;
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * @author Gayathery
     * This method collects data in a background thread initiated by the actual TrackerAdapter
     * It has its own datastructure and data is filled into this structure in background
     * This method is called whenever the listener thread finds a change in underlying data
     */
    public Boolean prepareData()
    {
        
        class TrackerAdapterWorkerCallable implements Callable<Collector>{
            
            Requirement requirement;
            public TrackerAdapterWorkerCallable(Requirement requirement){
                this.requirement = requirement;
                
            }

            @Override
            public Collector call() throws Exception {
                Collector collectWorker = new Collector();
                Collection<Commit> commits = getCommitsFromRequirement(requirement);
                collectWorker.workerRepositoryReqCommitFile.put(requirement, getCommitFilesForRequirement(requirement));
                collectWorker.workerRepositoryReqCommit.put(requirement,commits);
                for(Commit commit : commits){
                    Collection<CommitFile> commitFiles = getFilesFromCommit(commit);
                    collectWorker.workerRepositoryCommitCommitFile.put(commit,commitFiles);
                    collectWorker.totalCommitFiles.addAll(commitFiles);
                
                }
                return collectWorker;
            
            }
        }
        if(isThreadingRequired){
            ExecutorService executor = Executors.newCachedThreadPool();
            
            requirements = getAllRequirements();
            List<Future<Collector>> futureList = new ArrayList<Future<Collector>>();
            for(Requirement requirement : requirements){
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {                    
                    return false;
                }
                Future<Collector> future = executor.submit(new TrackerAdapterWorkerCallable(requirement));
                futureList.add(future);
                
            }
            
            for(Future<Collector> futureElement : futureList){
                collectFutures(futureElement);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {                    
                    return false;
                }
            }
            
            executor.shutdown();
        }
        else{
            
                requirements = getAllRequirements();
                for(Requirement requirement : requirements){
                    Collection<Commit> commits = getCommitsFromRequirement(requirement);
                    workerRepositoryReqCommitFile.put(requirement, getCommitFilesForRequirement(requirement));
                    workerRepositoryReqCommit.put(requirement,commits);
                    for(Commit commit : commits){
                        Collection<CommitFile> commitFiles = getFilesFromCommit(commit);
                        workerRepositoryCommitCommitFile.put(commit,commitFiles);
                        totalCommitFiles.addAll(commitFiles);
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {                    
                            return false;
                        }
                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {                    
                        return false;
                    }
                }
        }
        
        Calendar calc = Calendar.getInstance();
        CommitCount = getAllCommits().size();
        globalChangeTime = calc.getTime();
        getAllRequirements = globalChangeTime;
        getCommitsFromRequirement = globalChangeTime;
        getAllFiles = globalChangeTime;
        getRequirementsFromFile = globalChangeTime;
        getCommitFilesForRequirement = globalChangeTime;
        getImpactPercentageForCommitFileListAndRequirement = globalChangeTime;
        
        isReadyForTakeOver = true;
        return true;
        
    }
    
    
    /**
     * @author Gayathery
     * This method starts a listener thread to check the changes in underlying data
     * This method automatically polls the underlying data at a predefined interval
     * This method changes the globalChangeTime when it finds a change in the underlying data
     * and this in turns invalidates masking of methods
     */
    public void listen(){
        class TrackerAdapterWorkerListener extends Thread {

            public void run() {
                
                while(true){
                    long tempCount = getAllCommits().size();
                if(tempCount == CommitCount){
                    try {

                        Thread.sleep(5000);

                    } catch (InterruptedException e) {
                        
                        e.printStackTrace();
                        return;
                    }
                    continue;
                }
                    
                Calendar calc = Calendar.getInstance();
                globalChangeTime = calc.getTime();
                if(!prepareData())
                    break;
                
                }
            }

            

        }
        Thread trackerAdapterWorkerListenerThread = new Thread(new TrackerAdapterWorkerListener());
        trackerAdapterWorkerListenerThread.setName("TrackerAdapterWorkerListenerThread");
        trackerAdapterWorkerListenerThread.setPriority(Thread.MIN_PRIORITY);
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {                    
            return;
        }
        trackerAdapterWorkerListenerThread.start();

    }
    /**     
     * @return Collection<Requirement>
     * This method masks the getAllRequirements method of TrackerAdapter after successful data preparation
     * @author Gayathery
     */
    public Collection<Requirement> getAllRequirements() {
        if(getAllRequirements == globalChangeTime){
            return requirements;
        }       
        

        return trackerAdapter.getAllRequirements();
    }

    /**
     * @return Collection<Commit>
     * @param requirement
     * This method masks the getCommitsFromRequirement method of TrackerAdapter after successful data preparation
     * @author Gayathery
     */
    public Collection<Commit> getCommitsFromRequirement(Requirement requirement) {
        if(getCommitsFromRequirement == globalChangeTime){
            return workerRepositoryReqCommit.get(requirement);
        }
       return trackerAdapter.getCommitsFromRequirement(requirement);
    }



    /**
     * @return Collection<CommitFile>
     * @author Gayathery
     * This method masks the getAllFiles method of TrackerAdapter after successful data preparation     
     */
    public Collection<CommitFile> getAllFiles() {
        if(getAllFiles == globalChangeTime){
            Collection<CommitFile> commitFiles = new ArrayList<CommitFile>(); 
            workerRepositoryCommitCommitFile.forEach((k,v) -> commitFiles.addAll(v));
            return commitFiles;
        }
        
        return trackerAdapter.getAllFiles();
    }


    
    /**
     * @param file
     * @return
     * @author Gayathery
     *  Will be masked in future if performance improvement is required
     */
    public Collection<Requirement> getRequirementsFromFile(CommitFile file) {
        

        return trackerAdapter.getRequirementsFromFile(file);
    }

    
    /**
     * @param file
     * @return
     * @author Gayathery
     */
    public Collection<Commit> getCommitsFromFile(CommitFile file) {
       
        return trackerAdapter.getCommitsFromFile(file);
    }

    
    /**
      *@return float - the impact value
     * @param file
     * @param commit
     * @author Gayathery
     * This method masks the getImpactPercentageForCommitFileListAndRequirement method of TrackerAdapter after successful data preparation
     */
 
    public float getImpactPercentageForCommitFileListAndRequirement(CommitFile file, Commit commit){
        if(getImpactPercentageForCommitFileListAndRequirement == globalChangeTime){
            Collection<CommitFile> commitFiles = workerRepositoryReqCommitFile.get(commit.instanceRequirement);
            for(CommitFile commitFile : commitFiles){
                if(commitFile.newPath.getPath().equals(file.newPath.getPath()))
                    return commitFile.impact;
               
            }
            return (float)0.0;
        }
    
        return trackerAdapter.getImpactPercentageForCommitFileListAndRequirement(file, commit);
    }
    
    
    
    /**
     * @param commit
     * @return
     * @author Gayathery
     *  Will be masked in future if performance improvement is required
     */
    public Collection<CommitFile> getFilesFromCommit(Commit commit) {
        return trackerAdapter.getFilesFromCommit(commit);
    }

    
    /**
     * @return
     * @author Gayathery
     *  Will be masked in future if performance improvement is required
     */
    public Pattern getCurrentRequirementPattern() {
        return trackerAdapter.getCurrentRequirementPattern();
    }

    
    /**
     * @param reqPatternString
     * @author Gayathery
     *  Will be masked in future if performance improvement is required
     */
    public void setReqPatternString(Pattern reqPatternString) {
            trackerAdapter.setReqPatternString(reqPatternString);
    }

    
    /**
     * @return
     * @author Gayathery
     *  Will be masked in future if performance improvement is required
     */
    public String getCurrentRepositoryPath() {
        return trackerAdapter.getCurrentRepositoryPath();
    }

    
    /**
     * @return
     * @author Gayathery
     *  Will be masked in future if performance improvement is required
     */
    public Collection<Commit> getAllCommits() {
        

        return trackerAdapter.getAllCommits();
    }

    
    /**
     * @param commit
     * @return
     * @author Gayathery
     *  Will be masked in future if performance improvement is required
     */
    public Collection<Requirement> getRequirementsFromCommit(Commit commit) {
       return trackerAdapter.getRequirementsFromCommit(commit);
    }

    
    /**
     * @param requirement
     * @return Collection<CommitFile>
     * This method masks the getCommitFilesForRequirement method of TrackerAdapter after successful data preparation
     * @author Gayathery
     */
    public Collection<CommitFile> getCommitFilesForRequirement(Requirement requirement) {
        if(getCommitFilesForRequirement == globalChangeTime){
            return workerRepositoryReqCommitFile.get(requirement);
        }
        

        return trackerAdapter.getCommitFilesForRequirement(requirement);
    }

    
    /**
     * @param requirement
     * @param commit
     * @author Gayathery
     *  Will be masked in future if performance improvement is required
     */
    public void addRequirementCommitRelation(Requirement requirement, Commit commit) {
         trackerAdapter.addRequirementCommitRelation(requirement, commit);
    }

    
    /**
     * @param next
     * @return
     * @author Gayathery
     *  Will be masked in future if performance improvement is required
     */
    public Collection<AnnotatedLine> getAnnotatedLines(CommitFile next) {
        return trackerAdapter.getAnnotatedLines(next);
    }

    
    /**
     * @return
     * @author Gayathery
     *  Will be masked in future if performance improvement is required
     */
    public RequirementsTraceabilityMatrix generateRequirementsTraceability() {
        return trackerAdapter.generateRequirementsTraceability();
    }

    
    /**
     * @return
     * @author Gayathery
     *  Will be masked in future if performance improvement is required
     */
    public RequirementsTraceabilityMatrixByImpact generateRequirementsTraceabilityByImpact() {
        return trackerAdapter.generateRequirementsTraceabilityByImpact();
    }

    
    /**
     * @param id
     * @param title
     * @param description
     * @return
     * @author Gayathery
     *  Will be masked in future if performance improvement is required
     */
    public boolean updateRequirement(String id, String title, String description) {
        return trackerAdapter.updateRequirement(id, title, description);
    }

}
