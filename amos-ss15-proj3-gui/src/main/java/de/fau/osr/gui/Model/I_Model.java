package de.fau.osr.gui.Model;

import de.fau.osr.bl.RequirementsTraceabilityMatrix;
import de.fau.osr.bl.RequirementsTraceabilityMatrixByImpact;
import de.fau.osr.gui.Model.DataElements.AnnotatedLine;
import de.fau.osr.gui.Model.DataElements.Commit;
import de.fau.osr.gui.Model.DataElements.CommitFile;
import de.fau.osr.gui.Model.DataElements.Requirement;

import java.util.Collection;
import java.util.regex.Pattern;

public interface I_Model {

    Collection<Requirement> getAllRequirements();

    Collection<Commit> getCommitsFromRequirement(Requirement requirement);

    Collection<CommitFile> getAllFiles();

    Collection<Requirement> getRequirementsFromFile(CommitFile file);

    Collection<Commit> getCommitsFromFile(CommitFile file);

    Collection<CommitFile> getFilesFromCommit(Commit commit);

    Pattern getCurrentRequirementPattern();

    void setReqPatternString(Pattern reqPattern);

    String getCurrentRepositoryPath();

    Collection<Commit> getAllCommits();

    Collection<Requirement> getRequirementsFromCommit(Commit commit);
    
    float getImpactPercentageForCommitFileListAndRequirement(CommitFile file, Commit commit);
        

    Collection<CommitFile> getCommitFilesForRequirement(
            Requirement requirement);

    void addRequirementCommitRelation(Requirement requirement, Commit commit);

    Collection<AnnotatedLine> getAnnotatedLines(CommitFile next);

    RequirementsTraceabilityMatrix generateRequirementsTraceability();

    RequirementsTraceabilityMatrixByImpact generateRequirementsTraceabilityByImpact();

    boolean updateRequirement(String id, String title, String description);
}
