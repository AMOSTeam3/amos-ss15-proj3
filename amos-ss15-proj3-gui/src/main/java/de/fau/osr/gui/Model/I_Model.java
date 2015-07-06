package de.fau.osr.gui.Model;

import de.fau.osr.bl.RequirementsTraceabilityMatrix;
import de.fau.osr.bl.RequirementsTraceabilityMatrixByImpact;
import de.fau.osr.gui.Model.DataElements.*;

import java.util.Collection;
import java.util.regex.Pattern;

public interface I_Model {

    Collection<Requirement> getAllRequirements();

    Collection<Commit> getCommitsFromRequirement(Requirement requirement);

    Collection<PathDE> getFiles();

    Collection<Requirement> getRequirementsByFile(PathDE file);

    Collection<Commit> getCommitsByFile(PathDE file);

    Collection<PathDE> getFilesByCommit(Commit commit);

    Pattern getCurrentRequirementPattern();

    void setReqPatternString(Pattern reqPattern);

    String getCurrentRepositoryPath();

    Collection<Commit> getAllCommits();

    Collection<Requirement> getRequirementsFromCommit(Commit commit);

    float getImpactForRequirementAndFile(Requirement requ, PathDE path);

    Collection<PathDE> getFilesByRequirement(Requirement requirement);

    void addRequirementCommitRelation(Requirement requirement, Commit commit);

    Collection<AnnotatedLine> getAnnotatedLines(PathDE filePath);

    RequirementsTraceabilityMatrix generateRequirementsTraceability();

    RequirementsTraceabilityMatrixByImpact generateRequirementsTraceabilityByImpact();

    boolean updateRequirement(String id, String title, String description);
}
