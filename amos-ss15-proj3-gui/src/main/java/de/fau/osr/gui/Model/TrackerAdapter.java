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
import de.fau.osr.gui.util.GenericLock;

/**
 * Adapter for Tracker, implements I_Model
 * Created by Dmitry Gorelenkov on 14.06.2015.
 */
public class TrackerAdapter implements I_Model {
    private final Tracker tracker;
    private TrackerAdapterWorker trackerAdapterWorker;
    private Pattern reqPatternString;
    public GenericLock lock;
    private Thread trackerAdapterWorkerThread;
    
    public TrackerAdapter(Tracker tracker,Boolean isIndexingRequired) throws IOException {
        this.tracker = tracker;
        lock = new GenericLock();
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
        lock.lock();
        try{
            if(trackerAdapterWorker.isReadyForTakeOver){
                Collection<Requirement> collectionOfRequirements = trackerAdapterWorker.getAllRequirements();                
                return collectionOfRequirements;
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        finally{
            lock.unlock();    
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
        lock.lock();
        try{
            if(trackerAdapterWorker.isReadyForTakeOver){
                Collection<Commit> collectionOfCommit =  trackerAdapterWorker.getCommitsFromRequirement(requirement);
                return collectionOfCommit;
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        finally{
            lock.unlock();    
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
    public Collection<PathDE> getFiles() {
        try {
            return ElementsConverter.convertFiles(tracker.getFiles());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

//    @Override
//    public Collection<CommitFile> getAllFiles() {
//        lock.lock();
//        try{
//            if(trackerAdapterWorker.isReadyForTakeOver){
//                Collection<CommitFile> collectionOfCommitFile =  trackerAdapterWorker.getAllFiles();             
//                return collectionOfCommitFile;            
//            }            
//        }
//        catch(Exception ex){
//            ex.printStackTrace();
//        }
//        finally{
//            lock.unlock();    
//        }
//        return ElementsConverter.convertCommitFiles(tracker.getAllCommitFiles());
//    }


    @Override
    public Collection<Requirement> getRequirementsByFile(PathDE file) {
        try {
            return ElementsConverter.convertRequirements(tracker.getRequirementsByFile(file.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public Collection<Commit> getCommitsByFile(PathDE file) {
        try {
            return ElementsConverter.convertCommits(new HashSet<>(tracker.getCommitsForFile(file.toString())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public float getImpactForRequirementAndFile(Requirement requ, PathDE path) {
          if(trackerAdapterWorker.isReadyForTakeOver){
              return trackerAdapterWorker.getImpactPercentageForRequirementAndFile(requ, path);
          }

        return tracker.getImpactPercentageForFileAndRequirement(path.toString(), requ.getID());
    }

//    @Override
//    public float getImpactPercentageForCommitFileListAndRequirement(CommitFile file, Commit commit){
//        lock.lock();
//        try{
//            if(trackerAdapterWorker.isReadyForTakeOver){
//                float impact = trackerAdapterWorker.getImpactPercentageForCommitFileListAndRequirement(file,commit);                
//                return impact;
//            }
//        }
//        catch(Exception ex){
//            ex.printStackTrace();
//        }
//        finally{
//            lock.unlock();    
//        }
//        return tracker.getImpactPercentageForFileAndRequirement(file.newPath.getPath(),commit.instanceRequirement.getID());
//    }
    
    
    @Override
    public Collection<PathDE> getFilesByCommit(Commit commit) {
        try {
            return ElementsConverter.convertFiles(tracker.getFilesByCommit(commit.id));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Pattern getCurrentRequirementPattern(){
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
    public Collection<PathDE> getFilesByRequirement(Requirement requirement) {
        lock.lock();
        try{
            if(trackerAdapterWorker.isReadyForTakeOver){
                Collection<PathDE> collectionOfCommitFiles =  trackerAdapterWorker.getCommitFilesForRequirement(requirement);
             
                return collectionOfCommitFiles;
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        finally{
            lock.unlock();    
        }
        try {
            return ElementsConverter.convertFiles(tracker.getFilesByRequirement(requirement.getID()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public void addRequirementCommitRelation(Requirement requirement, Commit commit) throws Exception {
        tracker.addRequirementCommitRelation(requirement.getID(), commit.id);
    }

    @Override
    public Collection<AnnotatedLine> getAnnotatedLines(PathDE file) {
        try {
            return ElementsConverter.convertAnnotatedLines(tracker.getBlame(file.toString()));

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
