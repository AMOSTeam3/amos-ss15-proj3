package de.fau.osr.app;

import java.util.Iterator;
import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import de.fau.osr.core.vcs.base.VcsController;
import de.fau.osr.core.vcs.base.VcsEnvironment;
import de.fau.osr.util.parser.Parser;
import de.fau.osr.util.parser.CommitMessageParser;


/**
 * @author tobias
 * This is an application to fulfill the acceptance criterion for Req-4
 */
public class CommitRequirementsListApp {

	private static class CliOptions {
		@Parameter(names = "-repo", required = true)
		String repoURL;
	}

	public static void main(String[] args) {
		CliOptions cli = new CliOptions();
		new JCommander(cli, args);
		final VcsController controller = new VcsController(VcsEnvironment.GIT);
		Parser parser = new CommitMessageParser();
		controller.Connect(cli.repoURL);
		for(String commitId : new Iterable<String>(){
			@Override
			public Iterator<String> iterator() {
				return controller.getCommitList();
			}}) {
			String commitMsg = controller.getCommitMessage(commitId);
			List<Integer> reqs = parser.parse(commitMsg);
			for(Integer i : reqs) {
				System.out.println("commit " + commitId + " references Req-" + i);
			}
		}
	}

}
