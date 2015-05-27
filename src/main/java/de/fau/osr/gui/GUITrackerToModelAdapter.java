package de.fau.osr.gui;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import de.fau.osr.bl.RequirementsTraceabilityMatrix;
import de.fau.osr.bl.RequirementsTraceabilityMatrixByImpact;
import de.fau.osr.bl.Tracker;
import de.fau.osr.core.db.DataSource;
import de.fau.osr.core.vcs.base.Commit;
import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.interfaces.VcsClient;
import de.fau.osr.core.vcs.interfaces.VcsClient.AnnotatedLine;
import de.fau.osr.gui.GuiView.HighlightedLine;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Adapter class. Providing the correct formatted input for the Library Facade and transforming
 * the output to match the needed Interface.
 */
public class GUITrackerToModelAdapter implements GuiModel {
    Tracker tracker;
    private List<CommitFile> commitFiles;
    // TODO maybe we should use "List" instead of "Collection".
    private Collection<Commit> commits;
    private Pattern currentReqPattern;
    List<AnnotatedLine> currentBlame;

    public GUITrackerToModelAdapter(VcsClient vcs, DataSource ds, File repoFile, Pattern reqPatternString)
            throws IOException, RuntimeException {
        currentReqPattern = reqPatternString;
        tracker = new Tracker(vcs, ds, repoFile);
    }

    @Override
    public String[] getAllRequirements(Predicate<String> filtering) throws IOException {
        Collection<String> reqIds = tracker.getAllRequirements();
        String[] result = convertCollectionToArray(Collections2.filter(reqIds, filtering));
        //temp comparator, todo implement more universal one
        Arrays.sort(result, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.parseInt(o1) - Integer.parseInt(o2);
            }
        });

        return result;
    }

    @Override
    public String[] getCommitsFromRequirementID(String requirement) throws IOException {
        commits = tracker.getCommitsForRequirementID(requirement);
        return getMessagesFromCommits();
    }

    @Override
    public CommitFile[] getAllFiles(Comparator<CommitFile> sorting) {
        List<CommitFile> commitFiles = new ArrayList<CommitFile>(tracker.getAllFiles());
        Collections.sort(commitFiles, sorting);
        CommitFile[] returnValue = new CommitFile[commitFiles.size()];
        return commitFiles.toArray(returnValue);
    }

    @Override
    public String[] getRequirementsFromFile(CommitFile file) throws IOException {
        String filePathTransformed = file.newPath.getPath().toString().replace("\\", "/");
        Collection<String> requirements = tracker
                .getAllRequirementsForFile(filePathTransformed);
        return convertCollectionToArray(requirements);
    }


    @Override
    public String[] getCommitsFromFile(CommitFile file) {
        String filePathTransformed = file.newPath.getPath().toString().replace("\\", "/");
        commits = tracker.getCommitsFromFile(filePathTransformed);
        return getMessagesFromCommits();
    }

    @Override
    public CommitFile[] getFilesFromCommit(int commitIndex, Comparator<CommitFile> sorting)
            throws FileNotFoundException {
        List<CommitFile> commitFiles = getCommit(commitIndex).files;
        Collections.sort(commitFiles, sorting);
        CommitFile[] returnValue = new CommitFile[commitFiles.size()];
        return commitFiles.toArray(returnValue);
    }

    @Override
    public String getChangeDataFromFileIndex(CommitFile file)
            throws FileNotFoundException {

        return file.changedData;
    }

    @Override
    public String[] getCommitsFromDB() {
        commits = tracker.getCommits();
        return getMessagesFromCommits();
    }

    @Override
    public String[] getRequirementsFromCommit(int commitIndex)
            throws IOException {
        Set<String> collection = new HashSet<String>(tracker
                .getRequirementsFromCommit(getCommit(commitIndex)));
        return convertCollectionToArray(collection);
    }

    @Override
    public String[] commitsFromRequirementAndFile(String requirementID,
            CommitFile file) throws IOException {
        Set<String> commits1 = new HashSet<String>(Arrays.asList(getCommitsFromFile(file)));
        Set<String> commits2 = new HashSet<String>(Arrays.asList(getCommitsFromRequirementID(requirementID)));

        commits1.retainAll(commits2);
        return convertCollectionToArray(commits1);
    }

    @Override
    public String[] getRequirementsFromFileAndCommit(int commitIndex,
            CommitFile file) throws IOException {
        Set<String> requirements1 = new HashSet<String>(Arrays.asList(getRequirementsFromCommit(commitIndex)));
        Set<String> requirements2 = new HashSet<String>(Arrays.asList(getRequirementsFromFile(file)));

        requirements1.retainAll(requirements2);
        return convertCollectionToArray(requirements1);
    }

    @Override
    public CommitFile[] getFilesFromRequirement(String requirementID, Comparator<CommitFile> sorting) throws IOException {
        List<CommitFile> commitFiles = tracker.getCommitFilesForRequirementID(requirementID);
        Collections.sort(commitFiles, sorting);
        CommitFile[] returnValue = new CommitFile[commitFiles.size()];
        return commitFiles.toArray(returnValue);
    }

    @Override
    public void addRequirementCommitLinkage(String requirementID,
            int commitIndex) throws FileNotFoundException {
        String commitId = getCommit(commitIndex).id;
        try {
            tracker.addRequirementCommitRelation(requirementID, commitId);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    private Commit getCommit(int commitIndex) throws FileNotFoundException {
        int i = 0;
        for (Commit commit : commits) {
            if (i == commitIndex) {
                return commit;
            }
            i++;
        }
        throw new FileNotFoundException();
    }

    private String[] convertCollectionToArray(Collection<String> collection) {
        String[] array = new String[collection.size()];
        collection.toArray(array);
        return array;
    }

    private String[] getMessagesFromCommits() {
        String[] commitMessagesArray = new String[commits.size()];
        int i = 0;
        for (Commit commit : commits) {
            commitMessagesArray[i] = commit.message;
            i++;
        }
        return commitMessagesArray;
    }

    @Override
    public String getCurrentRequirementPatternString() {
        return currentReqPattern.pattern();
    }

    @Override
    public String getCurrentRepositoryPath() {
        return tracker.getCurrentRepositoryPath();
    }

    @Override
    public HighlightedLine[] getBlame(CommitFile file,
            String requirementID) throws FileNotFoundException, IOException, GitAPIException {
        currentBlame = tracker.getBlame(file.newPath.getPath());
        HighlightedLine[] hightlightedLines = new HighlightedLine[currentBlame.size()];
        int i= 0;
        for(AnnotatedLine line: currentBlame){
            hightlightedLines[i++] = new HighlightedLine(line.getLine(), line.getRequirements().contains(requirementID));
        }
        return hightlightedLines;
    }

    @Override
    public String[] getRequirementsForBlame(CommitFile file)
            throws FileNotFoundException, IOException, GitAPIException {
        Collection<AnnotatedLine> lines = tracker.getBlame(file.newPath.getPath());
        Collection<String> reqIdsByLines = new ArrayList<String>();

        for(AnnotatedLine line: lines){
            final Collection<String> requirements = line.getRequirements();
            reqIdsByLines.add(Joiner.on(",").join(requirements));
        }

        return convertCollectionToArray(reqIdsByLines);
    }

    @Override
    public String[] getRequirementsForBlame(int lineIndex, CommitFile file) throws FileNotFoundException, IOException, GitAPIException{
        Collection<AnnotatedLine> lines = tracker.getBlame(file.newPath.getPath());
        int i = 0;
        for(AnnotatedLine line: lines){
            if(i == lineIndex){
                return convertCollectionToArray(line.getRequirements());
            }
            i++;
        }
        throw new FileNotFoundException();
    }

    @Override
    public RequirementsTraceabilityMatrix getRequirementsTraceability()
            throws IOException {
        return tracker.generateRequirementsTraceability();
    }

    @Override
    public RequirementsTraceabilityMatrixByImpact getRequirementsTraceabilityByImpact()
            throws IOException {
        return tracker.generateRequirementsTraceabilityByImpact();
    }
}
