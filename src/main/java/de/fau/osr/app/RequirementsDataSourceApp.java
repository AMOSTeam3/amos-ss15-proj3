package de.fau.osr.app;

import java.util.Iterator;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import de.fau.osr.bl.Tracker;
import de.fau.osr.core.vcs.base.VcsController;
import de.fau.osr.core.vcs.base.VcsEnvironment;


public class RequirementsDataSourceApp {
	private static class CliOptions {
		@Parameter(names = "-repo", required = true)
		String repoURL;
		@Parameter(names = "-filepath", required = true)
		String filePath;
	}
	public static void main(String[] args) {
		CliOptions cli = new CliOptions();
		new JCommander(cli, args);
		VcsController vcs = new VcsController(VcsEnvironment.GIT);
		vcs.Connect(cli.repoURL);
		final Tracker requirementsList = new Tracker(vcs);
		final String filePath = cli.filePath;
		for(Integer requirementsList1: new Iterable<Integer>() {

			@Override
			public Iterator<Integer> iterator() {
				return requirementsList.getAllRerequirementsList(filePath).iterator();
			}}) {
			System.out.println(requirementsList1);
		}
	}
}
