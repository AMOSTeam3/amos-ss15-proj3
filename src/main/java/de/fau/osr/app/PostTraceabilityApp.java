package de.fau.osr.app;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import de.fau.osr.core.db.ReqCommitRelationDB;
import de.fau.osr.core.db.CSVFileReqCommitRelationDB;
import de.fau.osr.core.vcs.base.VcsController;
import de.fau.osr.core.vcs.base.VcsEnvironment;
import de.fau.osr.parser.CommitMessageParser;
import de.fau.osr.parser.GitCommitMessageParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Taleh Didover
 *
 * This is an application to fulfill the acceptance criterion for Req-13
 */
public class PostTraceabilityApp {

    private static String storageFileName = "req--commit.csv";

    private static class CliOptions {
        @Parameter(names = "-repo", required = true)
        String repoURL;
    }

    private static Integer readReqID(BufferedReader br) throws IOException {
        Integer reqID = null;
        boolean retry = true;

        while (retry) {

            try {
                System.out.print("Enter 1 requirement id: Req-");
                reqID = Integer.valueOf(br.readLine());
                retry = false;
            } catch (NumberFormatException err) {
                System.out.println("Input is invalid! Just enter an integer.");
            }
        }

        return Math.abs(reqID);
    }

    private static String readCommitID(BufferedReader br) throws IOException {
        String commitID = null;
        boolean retry = true;

        while (retry) {
                System.out.print("Enter 1 commit id: ");
                commitID = br.readLine().trim().toLowerCase();
                if (!commitID.isEmpty())
                    retry = false;
        }

        return commitID;
    }

    public static void main(String[] args) {
        CliOptions cli = new CliOptions();
        new JCommander(cli, args);
        final VcsController controller = new VcsController(VcsEnvironment.GIT);
        CommitMessageParser parser = new GitCommitMessageParser();

        Path repoFilePath = Paths.get(cli.repoURL);
        controller.Connect(repoFilePath.toString());


        Path storageFilePath = repoFilePath.getParent().resolve(storageFileName);
        ReqCommitRelationDB storage = new CSVFileReqCommitRelationDB(storageFilePath);

        System.out.format("Will store post traceability datas at '%s'%n", storageFilePath.toString());
        System.out.format("Exit mit CTRL-C%n%n");


        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean isRunning = true;

        while (isRunning) {
            Integer reqID = null;
            String commitID = null;

            try {
                reqID = readReqID(br);
            } catch (IOException ioe) {
                System.out.println("ERROR: An I/O error occured when reading Requirement ID.");
                System.exit(1);
            }

            try {
                commitID = readCommitID(br);
            } catch (IOException ioe) {
                System.out.println("ERROR: An I/O error occured during reading commit id.");
                System.exit(1);
            }

            storage.addFurtherDependency(reqID, commitID);

        }
    }
}
