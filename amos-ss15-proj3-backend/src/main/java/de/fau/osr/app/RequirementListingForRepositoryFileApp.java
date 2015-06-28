package de.fau.osr.app;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import de.fau.osr.bl.Tracker;
import de.fau.osr.core.vcs.base.VcsEnvironment;
import de.fau.osr.core.vcs.interfaces.VcsClient;

import java.io.IOException;
import java.util.Iterator;

/**
 * @author Gayathery
 * This is an application to fulfill the acceptance criterion for Req-8
 */
public class RequirementListingForRepositoryFileApp {
    private static class CliOptions {
        @Parameter(names = "-repo", required = true)
        String repoURL;
        @Parameter(names = "-filepath", required = true)
        String filePath;
    }
    public static void main(String[] args) throws IOException {
        CliOptions cli = new CliOptions();
        new JCommander(cli, args);
        VcsClient client = VcsClient.connect(VcsEnvironment.GIT, cli.repoURL);
        final Tracker requirementsTracer = new Tracker(client);
        final String filePath = cli.filePath;
        for(String requirementID : new Iterable<String>() {

            @Override
            public Iterator<String> iterator() {
                try {
                    return requirementsTracer.getRequirementIdsForFile(filePath).iterator();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }}) {
            System.out.println(requirementID);
        }
    }
}
