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
        Set<Integer> reqsList = tracker.getAllRequirementsForFile(cli.filePath);
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
