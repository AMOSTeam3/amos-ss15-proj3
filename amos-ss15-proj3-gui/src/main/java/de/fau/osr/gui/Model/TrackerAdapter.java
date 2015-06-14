package de.fau.osr.gui.Model;

import de.fau.osr.bl.RequirementsTraceabilityMatrix;
import de.fau.osr.bl.RequirementsTraceabilityMatrixByImpact;
import de.fau.osr.bl.Tracker;
import de.fau.osr.gui.Model.DataElements.AnnotatedLine;
import de.fau.osr.gui.Model.DataElements.Commit;
import de.fau.osr.gui.Model.DataElements.CommitFile;
import de.fau.osr.gui.Model.DataElements.Requirement;
import de.fau.osr.gui.util.ElementsConverter;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by Dmitry Gorelenkov on 14.06.2015.
 */
public class TrackerAdapter implements I_Model {


    private final Tracker tracker;
    private Pattern reqPatternString;

    public TrackerAdapter(Tracker tracker) throws IOException {
        this.tracker = tracker;
    }


    @Override
    public Collection<Requirement> getAllRequirements() {
        Collection<Requirement> reqsUI = new ArrayList<>();

        try {
            reqsUI = ElementsConverter.convertRequirements(tracker.getAllRequirementObjects());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return reqsUI;
    }

    @Override
    public Collection<Commit> getCommitsFromRequirement(Requirement requirement) {
        de.fau.osr.core.domain.Requirement req = tracker.getRequirementObjectById(requirement.getID());
        Set<de.fau.osr.core.domain.Commit> commitsForReq = req.getCommits();
        return ElementsConverter.convertCommits(commitsForReq);
    }



    @Override
    public Collection<CommitFile> getAllFiles() {
        return ElementsConverter.convertCommitFiles(tracker.getAllFiles());
    }


    @Override
    public Collection<Requirement> getRequirementsFromFile(CommitFile file) {
        Set<String> reqIds = new HashSet<>();
        Collection<de.fau.osr.core.domain.Requirement> reqs = new ArrayList<>();
        try {

            reqIds = tracker.getAllRequirementsForFile(file.newPath.getPath());

            reqs = tracker.getReqObjectsByIds(reqIds);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return ElementsConverter.convertRequirements(reqs);
    }

    @Override
    public Collection<Commit> getCommitsFromFile(CommitFile file) {
        return ElementsConverter.convertCommitsVCSCommits(new HashSet<>(tracker.getCommitsFromFile(file.newPath.getPath())));
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
        return ElementsConverter.convertCommitsVCSCommits(new HashSet<>(tracker.getCommits()));
    }

    @Override
    public Collection<Requirement> getRequirementsFromCommit(Commit commit) {
        Set<String> reqs;
        Collection<de.fau.osr.core.domain.Requirement> reqObjects = new ArrayList<>();

        try {
            reqs = tracker.getAllCommitReqRelations().get(commit.id);

            reqObjects = tracker.getReqObjectsByIds(reqs);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return ElementsConverter.convertRequirements(reqObjects);
    }

    @Override
    public Collection<CommitFile> getCommitFilesForRequirement(Requirement requirement) {
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


}
