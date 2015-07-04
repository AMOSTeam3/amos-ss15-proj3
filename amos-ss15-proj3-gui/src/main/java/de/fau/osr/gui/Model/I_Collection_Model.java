package de.fau.osr.gui.Model;

import com.google.common.base.Predicate;
import de.fau.osr.bl.RequirementsTraceabilityMatrix;
import de.fau.osr.bl.RequirementsTraceabilityMatrixByImpact;
import de.fau.osr.gui.Model.DataElements.Commit;
import de.fau.osr.gui.Model.DataElements.CommitFile;
import de.fau.osr.gui.Model.DataElements.DataElement;
import de.fau.osr.gui.Model.DataElements.Requirement;
import de.fau.osr.gui.Model.DataElements.PathDE;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Adapter Pattern used. Since the Functionlibrary itself
 * can not simply provide the given return types correlating to
 * the specific formatted input, this class defines the Interface
 * to be provided by the adapter
 */

public interface I_Collection_Model {

    Collection<? extends DataElement> getAllRequirements(Predicate<Requirement> requirementIDFiltering) throws IOException;

    Collection<? extends DataElement> getCommitsFromRequirementID(Collection<Requirement> requirements) throws IOException;

    List<? extends DataElement> getFilePaths();

    List<? extends DataElement> getAllFiles(Comparator<CommitFile> sorting);

    Collection<? extends DataElement> getRequirementsFromFile(Collection<CommitFile> files) throws IOException;

    Collection<? extends DataElement> getCommitsFromFile(Collection<CommitFile> files);

    List<? extends DataElement> getFilesByommit(Collection<Commit> commits) throws FileNotFoundException;

    String getChangeDataFromFileIndex(CommitFile file) throws FileNotFoundException;

    Pattern getCurrentRequirementPattern();

    void setCurrentRequirementPattern(Pattern pattern);

    String getCurrentRepositoryPath();

    Collection<? extends DataElement> getCommitsFromDB();

    Collection<? extends DataElement> getRequirementsFromCommit(Collection<Commit> commits) throws IOException;

    Collection<? extends DataElement> commitsFromRequirementAndFile(Collection<Requirement> requirements, Collection<CommitFile> commitFile) throws IOException;

    Collection<? extends DataElement> getRequirementsFromFileAndCommit(Collection<Commit> commits, Collection<CommitFile> files) throws IOException;

    List<? extends DataElement> getFilesByRequirement(Collection<Requirement> requirements) throws IOException;

    void addRequirementCommitLinkage(Requirement requirement, Commit commit) throws FileNotFoundException;

    Collection<? extends DataElement> AnnotatedLinesFromFile(Collection<CommitFile> files) throws FileNotFoundException, IOException, GitAPIException ;

    RequirementsTraceabilityMatrix getRequirementsTraceability() throws IOException;

    RequirementsTraceabilityMatrixByImpact getRequirementsTraceabilityByImpact() throws IOException;

    boolean updateRequirement(String id, String title, String description);
    
    List<DataElement> getImpactByRequirementAndPath(Collection<Requirement> requirements, PathDE path);
}
