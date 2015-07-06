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
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Collection_Model_Impl implements I_Collection_Model {
    private Collection<Commit> commits = null;
    private Collection<Requirement> requirements = null;
    private Collection<PathDE> filePaths = null;

    private I_Model model;

    private final Comparator<PathDE> SortFileByName = ((lhs,rhs) -> lhs.FilePath.compareTo(rhs.FilePath));

    public Collection_Model_Impl(I_Model model) {
        this.model = model;
    }
    
    @Override
    public Collection<? extends DataElement> getRequirements(
            Predicate<Requirement> filtering) throws IOException{
        if(requirements == null){
            requirements = model.getAllRequirements();
        }
        
        Collection<Requirement> filteredRequirements = Collections2.filter(requirements, filtering);
        return filteredRequirements; 
    }

    @Override
    public Collection<? extends DataElement> getCommitsByRequirement(
            Collection<Requirement> requirements) throws IOException {
        Collection<Commit> commits = new ArrayList<>();
        for(Requirement requirement: requirements){
            commits.addAll(model.getCommitsFromRequirement(requirement));
        }
        
        return commits;
    }

    @Override
    public List<? extends DataElement> getFilePaths() {
        if(filePaths == null){
            filePaths = model.getFiles();
        }

        List<PathDE> commitsSorted = new ArrayList<>(filePaths);
        commitsSorted.sort(SortFileByName);

        return commitsSorted;
    }

    @Override
    public Collection<? extends DataElement> getRequirementsByFile(
            Collection<PathDE> files) throws IOException {
        
        Collection<Requirement> requirements = new ArrayList<>();
        for(PathDE file: files){
            requirements.addAll(model.getRequirementsByFile(file));
        }
        
        return requirements;
    }

    @Override
    public Collection<? extends DataElement> getCommitsByFile(
            Collection<PathDE> files) {
        
        Collection<Commit> commits = new ArrayList<>();
        for(PathDE file: files){
            commits.addAll(model.getCommitsByFile(file));
        }
        
        return commits;
    }

    @Deprecated
    @Override
    public List<? extends DataElement> getFilesByCommit(
            Collection<Commit> commits)
            throws FileNotFoundException {
        
        List<PathDE> files = new ArrayList<>();
        commits.forEach(ci -> {
                    files.addAll(model.getFilesByCommit(ci));
                }
        );

        return files.stream()
                .distinct()
                .sorted(SortFileByName)
                .collect(Collectors.toList());
    }

    @Override
    public String getChangeDataFromFileIndex(PathDE file)
            throws FileNotFoundException {
        // TODO Auto-generated method stub
        // TODO I think, we should delete this method from interfaces (its never used)
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
    public Collection<? extends DataElement> getRequirementsByCommit(
            Collection<Commit> commits) throws IOException {
        
        Collection<Requirement> requirements = new ArrayList<>();
        for(Commit commit: commits){
            requirements.addAll(model.getRequirementsFromCommit(commit));
        }
        //TODO Should we explicitly make requirements distinct?
        return requirements;
    }

    @Override
    public Collection<? extends DataElement> getCommitsByRequirementAndFile(
            Collection<Requirement> requirements,
            Collection<PathDE> files) throws IOException {

        Collection<? extends DataElement> commits1 = this.getCommitsByRequirement(requirements);
        Collection<? extends DataElement> commits2 = this.getCommitsByFile(files);
        
        commits1.retainAll(commits2);
        return commits1;
    }

    @Override
    public Collection<? extends DataElement> getRequirementsByFileAndCommit(
            Collection<Commit> commits, Collection<PathDE> files)
            throws IOException {

        Collection<? extends DataElement> requirements1 = this.getRequirementsByCommit(commits);
        Collection<? extends DataElement> requirements2 = this.getRequirementsByFile(files);
        
        requirements1.retainAll(requirements2);
        return requirements2;
    }

    @Override
    public List<? extends DataElement> getFilesByRequirement(Collection<Requirement> requirements)
            throws IOException {

        List<PathDE> files = new ArrayList<>();
        requirements.forEach(req -> {
                    files.addAll(model.getFilesByRequirement(req));
                }
        );
        Collections.sort(files, SortFileByName);
        
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
    public Collection<? extends DataElement> getAnnotatedLinesByFile(Collection<PathDE> files)
            throws FileNotFoundException, IOException, GitAPIException {
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

    @Override
    public List<DataElement> getImpactForRequirementAndFile(Collection<Requirement> requirements, List<PathDE> path) {
        // TODO Should *path* really be a List<PathDE> instead just PathDE.
        Function<Requirement, ImpactDE> impactMapper;
        if (path.size() > 0)
            impactMapper = (Requirement req) -> {
                return new ImpactDE(model.getImpactForRequirementAndFile(req, path.get(0)));
            };
        else
            impactMapper = req -> {return new ImpactDE((float) 0.);};

        return requirements.stream()
                .map(impactMapper)
                .collect(Collectors.toList());
    }

}
