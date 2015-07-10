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
package de.fau.osr.app;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import de.fau.osr.bl.Tracker;
import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.VcsEnvironment;
import de.fau.osr.core.vcs.interfaces.VcsClient;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * File to show code lines by give requirement and file
 * Created by Dmitry Gorelenkov on 05.05.2015.
 */
public class ShowCodeLinesByReqByFileApp {
    private static class CliOptions {
        @Parameter(names = "-repo", required = true)
        String repoURL;
        @Parameter(names = "-req", required = true)
        String reqId;
        @Parameter(names = "-file", required = true)
        String filePath;

    }

    public static void main(String[] args) throws IOException {
        CliOptions cli = new CliOptions();
        new JCommander(cli, args);
        final VcsClient client = VcsClient.connect(VcsEnvironment.GIT, cli.repoURL);
        final String reqId = cli.reqId;

        Tracker tracker = new Tracker(client);

        //test if there are any reqs linked to the file
        Set<String> reqsList = tracker.getRequirementIdsForFile(cli.filePath);
        if (!reqsList.contains(Integer.parseInt(reqId))) {
            System.out.println("No requirement " + reqId + " linked to this file");
            return;
        }

        //get commitFiles (from different commits) to the file, we looking for.
        List<CommitFile> allCommitFiles = tracker.getCommitFilesForRequirementID(reqId);
        List<CommitFile> commitFiles = new ArrayList<>();
        for (CommitFile commitFile : allCommitFiles) {
            if (commitFile.newPath.equals(new File(cli.filePath))) {
                commitFiles.add(commitFile);
            }
        }

        //show all commits changes. Todo merge the changes?
        for (CommitFile commitFile : commitFiles) {
            System.out.println(commitFile.changedData);
        }

    }
}
