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
package de.fau.osr.gui.util;

import de.fau.osr.gui.Model.DataElements.*;

import java.nio.file.Path;
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
     * converts collection of vcs Paths to UI Paths
     * @return collection of UI CommitFiles
     */
    public static Collection<PathDE> convertFiles(Collection<Path> filePaths) {
        ArrayList<PathDE> convertedFilePaths = new ArrayList<>();
        convertedFilePaths.addAll(filePaths.stream().map(PathDE::new).collect(Collectors.toList()));
        return convertedFilePaths;
    }

    /**
     * converts domain Commit to UI Commit data object
     * @param commits collection of domain commits
     * @return collection of UI Commits
     */
    public static Collection<Commit> convertCommits(Set<de.fau.osr.core.vcs.base.Commit> commits) {
        ArrayList<Commit> newCommits = new ArrayList<>();
        for (de.fau.osr.core.vcs.base.Commit commit : commits) {
            newCommits.add(new Commit(commit));
        }
        return newCommits;
    }

    /**
     * converts domain Requirements to UI Requirements
     * @param reqs collection of domain Requirements
     * @return collection of UI Requirements
     */
    public static Collection<Requirement> convertRequirements(Collection<de.fau.osr.core.Requirement> reqs) {
        ArrayList<Requirement> newReqs = new ArrayList<>();
        for (de.fau.osr.core.Requirement req : reqs) {
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
