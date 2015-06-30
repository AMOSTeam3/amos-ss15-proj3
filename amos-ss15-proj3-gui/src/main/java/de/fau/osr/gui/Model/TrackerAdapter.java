package de.fau.osr.gui.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.naming.OperationNotSupportedException;

import de.fau.osr.gui.Model.DataElements.*;
import org.eclipse.jgit.api.errors.GitAPIException;

import de.fau.osr.bl.RequirementsTraceabilityMatrix;
import de.fau.osr.bl.RequirementsTraceabilityMatrixByImpact;
import de.fau.osr.bl.Tracker;
import de.fau.osr.gui.util.ElementsConverter;

/**
 * Adapter for Tracker, implements I_Model
 * Created by Dmitry Gorelenkov on 14.06.2015.
 */
public class TrackerAdapter implements I_Model {


    private final Tracker tracker;
    private TrackerAdapterWorker trackerAdapterWorker;
    private Pattern reqPatternString;

    private Thread trackerAdapterWorkerThread;
    
    public TrackerAdapter(Tracker tracker,Boolean isIndexingRequired) throws IOException {
        this.tracker = tracker;
        trackerAdapterWorker = new TrackerAdapterWorker(this);
        
        class TrackerAdapterWorkerThread extends Thread {

            public void run() {
                if(trackerAdapterWorker.prepareData()){
                trackerAdapterWorker.listen();
                }
            }
        }
        resetWorkerThread();
        if(isIndexingRequired){
           
            trackerAdapterWorkerThread = new Thread(new TrackerAdapterWorkerThread());
            trackerAdapterWorkerThread.setPriority(Thread.MIN_PRIORITY);
            trackerAdapterWorkerThread.setName("TrackerAdapterWorkerThread");
            trackerAdapterWorkerThread.start();
        }
    }


    @Override
    public Collection<Requirement> getAllRequirements() {
        if(trackerAdapterWorker.isReadyForTakeOver){
            return trackerAdapterWorker.getAllRequirements();
        }
            
        Collection<Requirement> reqsUI = new ArrayList<>();

        try {
            reqsUI = ElementsConverter.convertRequirements(tracker.getRequirements());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return reqsUI;
    }

    @Override
    public Collection<Commit> getCommitsFromRequirement(Requirement requirement) {
        if(trackerAdapterWorker.isReadyForTakeOver){
            return trackerAdapterWorker.getCommitsFromRequirement(requirement);
        }
        de.fau.osr.core.Requirement req;
        Set<de.fau.osr.core.vcs.base.Commit> commitsForReq;
        try {
            req = tracker.getRequirementById(requirement.getID());
            commitsForReq = tracker.getCommitsForRequirementID(req.getId());
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        Collection<Commit> commitList = ElementsConverter.convertCommits(commitsForReq);
        for(Commit commit : commitList)
        {
            commit.instanceRequirement = requirement;
        }
        return commitList;
    }

    @Override
    public Collection<PathDE> getFilePaths() {
        try {
            return ElementsConverter.convertFilePaths(tracker.getFilePaths());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }


    @Override
    public Collection<CommitFile> getAllFiles() {
        if(trackerAdapterWorker.isReadyForTakeOver){
            return trackerAdapterWorker.getAllFiles();
        }
        return ElementsConverter.convertCommitFiles(tracker.getAllCommitFiles());
    }


    @Override
    public Collection<Requirement> getRequirementsFromFile(CommitFile file) {
        Set<String> reqIds = new HashSet<>();
        Collection<de.fau.osr.core.Requirement> reqs = new ArrayList<>();
        try {

            reqIds = tracker.getRequirementIdsForFile(file.newPath.getPath());

            reqs = tracker.getRequirementsByIds(reqIds);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return ElementsConverter.convertRequirements(reqs);
    }

    @Override
    public Collection<Commit> getCommitsFromFile(CommitFile file) {
        try {
            return ElementsConverter.convertCommits(new HashSet<>(tracker.getCommitsForFile(file.newPath.getPath())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    
    @Override
    public float getImpactPercentageForCommitFileListAndRequirement(CommitFile file, Commit commit){
        if(trackerAdapterWorker.isReadyForTakeOver){
            return trackerAdapterWorker.getImpactPercentageForCommitFileListAndRequirement(file,commit);
        }
    
        return tracker.getImpactPercentageForFileAndRequirement(file.newPath.getPath(),commit.instanceRequirement.getID());
    }
    
    
    @Override
    public Collection<CommitFile> getFilesFromCommit(Commit commit) {
        return commit.files;
    }

    @Override
    public Pattern getCurrentRequirementPattern() {
        return reqPatternString;
    }

    @Override
    public void setReqPatternString(Pattern reqPatternString) {
        this.reqPatternString = reqPatternString;
    }

    @Override
    public String getCurrentRepositoryPath() {
        return tracker.getCurrentRepositoryPath();
    }

    @Override
    public Collection<Commit> getAllCommits() {
        try {
            return ElementsConverter.convertCommits(new HashSet<>(tracker.getCommits()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public Collection<Requirement> getRequirementsFromCommit(Commit commit) {
        Set<String> reqs;
        Collection<de.fau.osr.core.Requirement> reqObjects = new ArrayList<>();

        try {
            reqs = tracker.getAllCommitReqRelations().get(commit.id);

            reqObjects = tracker.getRequirementsByIds(reqs);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return ElementsConverter.convertRequirements(reqObjects);
    }

    @Override
    public Collection<CommitFile> getCommitFilesForRequirement(Requirement requirement) {
        if(trackerAdapterWorker.isReadyForTakeOver){
            return trackerAdapterWorker.getCommitFilesForRequirement(requirement);
        }
        try {
            return ElementsConverter.convertCommitFiles(tracker.getCommitFilesForRequirementID(requirement.getID()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public void addRequirementCommitRelation(Requirement requirement, Commit commit) {
        try {
            tracker.addRequirementCommitRelation(requirement.getID(), commit.id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<AnnotatedLine> getAnnotatedLines(CommitFile next) {
        try {
            return ElementsConverter.convertAnnotatedLines(tracker.getBlame(next.newPath.getPath()));

        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public RequirementsTraceabilityMatrix generateRequirementsTraceability() {
        try {
            return tracker.generateRequirementsTraceability();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public RequirementsTraceabilityMatrixByImpact generateRequirementsTraceabilityByImpact() {
        return tracker.generateRequirementsTraceabilityByImpact();
    }

    @Override
    public boolean updateRequirement(String id, String title, String description) {
        try {
            tracker.saveOrUpdateRequirement(id, title, description);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OperationNotSupportedException e) {
            e.printStackTrace();
        }

        return false;
    }
    
    private void resetWorkerThread(){
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for ( Thread thread : threadSet ){
            if ( thread.getName( ).equals( "TrackerAdapterWorkerThread" ) || thread.getName( ).equals( "TrackerAdapterWorkerListenerThread" ))
                thread.interrupt();
        }
    }

}
