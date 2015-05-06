package de.fau.osr.app;

import java.util.Iterator;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.VcsEnvironment;
import de.fau.osr.core.vcs.interfaces.VcsClient;

/**
 * @author tobias
 * This is an application to fulfill the acceptance criterion for Req-5
 */
public class CommitFileListingApp {
	private static class CliOptions {
		@Parameter(names = "-repo", required = true)
		String repoURL;
		@Parameter(names = "-commit", required = true)
		String commitId;
	}
	public static void main(String[] args) {
		CliOptions cli = new CliOptions();
		new JCommander(cli, args);
		final VcsClient client = VcsClient.connect(VcsEnvironment.GIT, cli.repoURL);
		final String commitId = cli.commitId;
		for(CommitFile file : new Iterable<CommitFile>() {

			@Override
			public Iterator<CommitFile> iterator() {
				return client.getCommitFiles(commitId).iterator();
			}}) {
			System.out.println(file.oldPath + " " + file.commitState + " " + file.newPath);
		}
	}
}
