package de.fau.osr.app;

import java.util.Iterator;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import de.fau.osr.bl.Tracker;
import de.fau.osr.core.vcs.base.VcsEnvironment;
import de.fau.osr.core.vcs.interfaces.VcsClient;

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
	public static void main(String[] args) {
		CliOptions cli = new CliOptions();
		new JCommander(cli, args);
		VcsClient client = VcsClient.connect(VcsEnvironment.GIT, cli.repoURL);
		final Tracker requirementsTracer = new Tracker(client);
		final String filePath = cli.filePath;
		for(Integer requirementID : new Iterable<Integer>() {

			@Override
			public Iterator<Integer> iterator() {
				return requirementsTracer.getAllRequirementsforFile(filePath).iterator();
			}}) {
			System.out.println(requirementID);
		}
	}
}
