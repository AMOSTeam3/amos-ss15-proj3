package de.fau.osr.app;

import java.io.File;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import de.fau.osr.bl.VcsInterpreter;
import de.fau.osr.core.vcs.base.VcsController;
import de.fau.osr.core.vcs.base.VcsEnvironment;

public class RequirementsListingForFileApp {
	private static class CliOptions {
		@Parameter(names = "-repo", required = true)
		String repoURL;
		@Parameter(names = "-file", required = true)
		String file;
	}
	public static void main(String[] args) {
		CliOptions cli = new CliOptions();
		new JCommander(cli, args);
		VcsController vcs = new VcsController(VcsEnvironment.GIT);
		vcs.Connect(cli.repoURL);
		final VcsInterpreter interpreter = new VcsInterpreter(vcs);
		for(Integer req : interpreter.getRequirementsForFile(new File(cli.file))) {
			System.out.println(req);
		}
	}
}
