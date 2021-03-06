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

        Collection<Requirement> requirements = files.stream()
                .map(file -> model.getRequirementsByFile(file))
                .flatMap(reqs -> reqs.stream())
                .collect(Collectors.toList());

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
        return requirements1;
    }

    @Override
    public List<? extends DataElement> getFilesByCommitAndRequirement(
            Collection<Commit> commits,
            Collection<Requirement> requirements) throws IOException {
        List<? extends DataElement> files1 = this.getFilesByCommit(commits);
        List<? extends DataElement> files2 = this.getFilesByRequirement(requirements);

        files1.retainAll(files2);
        return files1;
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
            Commit commit) throws Exception {
        model.addRequirementCommitRelation(requirement, commit);
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
    public List<DataElement> getImpactForRequirementAndFile(Collection<Requirement> requirements, List<PathDE> paths) {
        List<DataElement> impact = new ArrayList<DataElement>();
        for(PathDE path: paths){
            float MaxImpact = 0;
            for(Requirement requirement: requirements){
                float impactPerRequirement =  model.getImpactForRequirementAndFile(requirement, path);
                if(impactPerRequirement > MaxImpact){
                    MaxImpact = impactPerRequirement;
                }
            }
            impact.add(new ImpactDE(MaxImpact));
        }
        
        return impact;
    }

}
