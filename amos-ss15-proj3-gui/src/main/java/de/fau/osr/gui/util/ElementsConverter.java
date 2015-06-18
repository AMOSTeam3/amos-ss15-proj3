package de.fau.osr.gui.util;

import com.beust.jcommander.internal.Lists;
import de.fau.osr.gui.Model.DataElements.AnnotatedLine;
import de.fau.osr.gui.Model.DataElements.Commit;
import de.fau.osr.gui.Model.DataElements.CommitFile;
import de.fau.osr.gui.Model.DataElements.Requirement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Useful methods to convert BusinessLogic data objects to UI data objects
 * Created by Dmitry Gorelenkov on 14.06.2015.
 */
public class ElementsConverter {

    /**
     * converts collection of vcs CommitFiles to UI CommitFile
     * @return collection of UI CommitFiles
     */
    public static Collection<CommitFile> convertCommitFiles(Collection<de.fau.osr.core.vcs.base.CommitFile> commitFiles) {
        ArrayList<CommitFile> newCommitFiles = new ArrayList<>();
        newCommitFiles.addAll(commitFiles.stream().map(CommitFile::new).collect(Collectors.toList()));
        return newCommitFiles;
    }

    /**
     * converts domain Commit to UI Commit data object
     * @param commits collection of domain commits
     * @return collection of UI Commits
     */
    public static Collection<Commit> convertCommits(Set<de.fau.osr.core.domain.Commit> commits) {
        ArrayList<Commit> newCommits = new ArrayList<>();
        for (de.fau.osr.core.domain.Commit commit : commits) {
            newCommits.add(new Commit(commit));
        }
        return newCommits;
    }

    /**
     * converts VCS Commits to UI Commits
     * @param commits collection of VCS Commits
     * @return collection of UI Commits
     */
    public static Collection<Commit> convertCommitsVCSCommits(Set<de.fau.osr.core.vcs.base.Commit> commits) {
        ArrayList<Commit> newCommits = new ArrayList<>();
        for (de.fau.osr.core.vcs.base.Commit commit : commits) {
            newCommits.add(new Commit(commit.id, commit.message, Lists.newArrayList(convertCommitFiles(commit.files))));
        }
        return newCommits;
    }

    /**
     * converts domain Requirements to UI Requirements
     * @param reqs collection of domain Requirements
     * @return collection of UI Requirements
     */
    public static Collection<Requirement> convertRequirements(Collection<de.fau.osr.core.domain.Requirement> reqs) {
        ArrayList<Requirement> newReqs = new ArrayList<>();
        for (de.fau.osr.core.domain.Requirement req : reqs) {
            newReqs.add(new Requirement(req));
        }
        return newReqs;
    }

    /**
     * converts Tracker AnnotatedLine to UI AnnotatedLine
     * @param annLines collection of tracker AnnotatedLines
     * @return collection of UI AnnotatedLines
     */
    public static Collection<AnnotatedLine> convertAnnotatedLines(Collection<de.fau.osr.core.vcs.AnnotatedLine> annLines) {
        ArrayList<AnnotatedLine> newAnnLines = new ArrayList<>();
        for (de.fau.osr.core.vcs.AnnotatedLine annLine : annLines) {
            newAnnLines.add(new AnnotatedLine(annLine));
        }
        return newAnnLines;
    }
}
