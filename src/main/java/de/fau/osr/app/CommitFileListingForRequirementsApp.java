package de.fau.osr.app;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import de.fau.osr.bl.Tracker;
import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.VcsEnvironment;
import de.fau.osr.core.vcs.interfaces.VcsClient;

import java.io.IOException;
import java.util.Iterator;

/**
 * @author Gayathery
 * This is an application to fulfill the acceptance criterion for Req-7
 */
public class CommitFileListingForRequirementsApp {
	private static class CliOptions {
		@Parameter(names = "-repo", required = true)
		String repoURL;
		@Parameter(names = "-req", required = true)
		String reqId;
	}
	public static void main(String[] args) throws IOException {
		CliOptions cli = new CliOptions();
		new JCommander(cli, args);
		VcsClient client = VcsClient.connect(VcsEnvironment.GIT, cli.repoURL);
		final Tracker interpreter = new Tracker(client);
		final String reqId = cli.reqId;
		for(CommitFile file : new Iterable<CommitFile>() {

			@Override
			public Iterator<CommitFile> iterator() {
				try {
					return interpreter.getCommitFilesForRequirementID(reqId).iterator();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}}) {
			System.out.println(file.oldPath + " " + file.commitState + " " + file.newPath +  " " + file.commitID);
		}
	}
}
