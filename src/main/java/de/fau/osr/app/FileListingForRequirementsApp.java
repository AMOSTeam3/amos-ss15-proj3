package de.fau.osr.app;

import java.io.File;
import java.util.Iterator;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import de.fau.osr.bl.VcsInterpreter;
import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.VcsController;
import de.fau.osr.core.vcs.base.VcsEnvironment;

/**
 * @author Gayathery
 * This is an application to fulfill the acceptance criterion for Req-7
 */
public class FileListingForRequirementsApp {
	private static class CliOptions {
		@Parameter(names = "-repo", required = true)
		String repoURL;
		@Parameter(names = "-req", required = true)
		String reqId;
	}
	public static void main(String[] args) {
		CliOptions cli = new CliOptions();
		new JCommander(cli, args);
		VcsController vcs = new VcsController(VcsEnvironment.GIT);
		vcs.Connect(cli.repoURL);
		final VcsInterpreter interpreter = new VcsInterpreter(vcs);
		final String reqId = cli.reqId;
		for(File file : interpreter.getCommitFilesForRequirementID(Integer.parseInt(reqId))) {
			System.out.println(file);
		}
	}
}
