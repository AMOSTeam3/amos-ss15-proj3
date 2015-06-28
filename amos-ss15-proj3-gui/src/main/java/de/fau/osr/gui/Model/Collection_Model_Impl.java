package de.fau.osr.gui.Model;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import de.fau.osr.bl.RequirementsTraceabilityMatrix;
import de.fau.osr.bl.RequirementsTraceabilityMatrixByImpact;
import de.fau.osr.gui.Model.DataElements.*;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class Collection_Model_Impl implements I_Collection_Model {
    private Collection<Commit> commits = null;
    private Collection<Requirement> requirements = null;
    private Collection<CommitFile> files = null;
    
    private I_Model model;

    public Collection_Model_Impl(I_Model model) {
        this.model = model;
    }
    
    @Override
    public Collection<? extends DataElement> getAllRequirements(
            Predicate<Requirement> filtering) throws IOException{
        if(requirements == null){
            requirements = model.getAllRequirements();
        }
        
        Collection<Requirement> filteredRequirements = Collections2.filter(requirements, filtering);
        return filteredRequirements; 
    }

    @Override
    public Collection<? extends DataElement> getCommitsFromRequirementID(
            Collection<Requirement> requirements) throws IOException {
        Collection<Commit> commits = new ArrayList<Commit>();
        for(Requirement requirement: requirements){
            commits.addAll(model.getCommitsFromRequirement(requirement));
        }
        
        return commits;
    }

    @Override
    public List<? extends DataElement> getAllFiles(Comparator<CommitFile> sorting) {
        if(files == null){
            files = model.getAllFiles();
        }
        
        List<CommitFile> commitsSorted = new ArrayList<CommitFile>(files); 
                
        Collections.sort(commitsSorted, sorting);
        
        return commitsSorted;
    }

    @Override
    public Collection<? extends DataElement> getRequirementsFromFile(
            Collection<CommitFile> files) throws IOException {
        
        Collection<Requirement> requirements = new ArrayList<Requirement>();
        for(CommitFile file: files){
            requirements.addAll(model.getRequirementsFromFile(file));
        }
        
        return requirements;
    }

    @Override
    public Collection<? extends DataElement> getCommitsFromFile(
            Collection<CommitFile> files) {
        
        Collection<Commit> commits = new ArrayList<Commit>();
        for(CommitFile file: files){
            commits.addAll(model.getCommitsFromFile(file));
        }
        
        return commits;
    }

    @Override
    public Collection<? extends DataElement> getFilesFromCommit(
            Collection<Commit> commits, Comparator<CommitFile> sorting)
            throws FileNotFoundException {
        
        List<CommitFile> commitsSorted = new ArrayList<CommitFile>();
        for(Commit commit: commits){
            Collection<CommitFile> tempCommits = model.getFilesFromCommit(commit);
            for(CommitFile commitFile: tempCommits){
                commitFile.impact = model.getImpactPercentageForCommitFileListAndRequirement(commitFile,commit);
             }
            commitsSorted.addAll(tempCommits);
        }
        
                
        Collections.sort(commitsSorted, sorting);
        
        return commitsSorted;
    }

    @Override
    public String getChangeDataFromFileIndex(CommitFile file)
            throws FileNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Pattern getCurrentRequirementPattern() {
        return model.getCurrentRequirementPattern();
    }
    @Override
    public void setCurrentRequirementPattern(Pattern pattern) {
        model.setReqPatternString(pattern);
    }

    @Override
    public String getCurrentRepositoryPath() {
        return model.getCurrentRepositoryPath();
    }

    @Override
    public Collection<? extends DataElement> getCommitsFromDB() {
        if(commits == null){
            commits = model.getAllCommits();
        }
        
        return commits;
    }

    @Override
    public Collection<? extends DataElement> getRequirementsFromCommit(
            Collection<Commit> commits) throws IOException {
        
        Collection<Requirement> requirements = new ArrayList<Requirement>();
        for(Commit commit: commits){
            requirements.addAll(model.getRequirementsFromCommit(commit));
        }
        
        return requirements;
    }

    @Override
    public Collection<? extends DataElement> commitsFromRequirementAndFile(
            Collection<Requirement> requirements,
            Collection<CommitFile> files) throws IOException {

        Collection<? extends DataElement> commits1 = this.getCommitsFromRequirementID(requirements);
        
        Collection<? extends DataElement> commits2 = this.getCommitsFromFile(files);
        
        commits1.retainAll(commits2);
        return commits1;
    }

    @Override
    public Collection<? extends DataElement> getRequirementsFromFileAndCommit(
            Collection<Commit> commits, Collection<CommitFile> files)
            throws IOException {

        Collection<? extends DataElement> requirements1 = this.getRequirementsFromCommit(commits);
        
        Collection<? extends DataElement> requirements2 = this.getRequirementsFromFile(files);
        
        requirements1.retainAll(requirements2);
        return requirements2;
    }

    @Override
    public Collection<? extends DataElement> getFilesFromRequirement(
            Collection<Requirement> requirements, Comparator<CommitFile> sorting)
            throws IOException {

        List<CommitFile> files = new ArrayList<CommitFile>();
        for(Requirement requirement: requirements){
            files.addAll(model.getCommitFilesForRequirement(requirement));
        }
        
        Collections.sort(files, sorting);
        
        return files;
    }

    @Override
    public void addRequirementCommitLinkage(Requirement requirement,
            Commit commit) throws FileNotFoundException {
        try {
            model.addRequirementCommitRelation(requirement, commit);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Collection<? extends DataElement> AnnotatedLinesFromFile(
            Collection<CommitFile> files) throws FileNotFoundException,
            IOException, GitAPIException {
        
        Collection<AnnotatedLine> lines = new ArrayList<AnnotatedLine>();
        lines.addAll(model.getAnnotatedLines(files.iterator().next()));
        
        return lines;
    }

    @Override
    public RequirementsTraceabilityMatrix getRequirementsTraceability()
            throws IOException {
        return model.generateRequirementsTraceability();
    }

    @Override
    public RequirementsTraceabilityMatrixByImpact getRequirementsTraceabilityByImpact()
            throws IOException {
        return model.generateRequirementsTraceabilityByImpact();
    }

    @Override
    public boolean updateRequirement(String id, String title, String description) {
        return model.updateRequirement(id, title, description);
    }

}
