package de.fau.osr.bl;

import java.util.HashSet;
import java.util.Iterator;

import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.VcsController;
import de.fau.osr.core.vcs.base.VcsEnvironment;
import de.fau.osr.parser.CommitMessageParser;
import de.fau.osr.parser.GitCommitMessageParser;
/**
 * @author Gayathery
 * @desc This class is an interpreter for data from Vcs
 *
 */
public class VcsInterpreter {

	VcsController vcsController;
	String repositoryUri;
	boolean isVcsControllerConnected = false;
	public VcsInterpreter(VcsEnvironment env, String repositoryUri)
	{
		vcsController = new VcsController(env);
		this.repositoryUri = repositoryUri;
		isVcsControllerConnected = vcsController.Connect(repositoryUri);
	}
	/* (non-Javadoc)
	 * @see de.fau.osr.bl.VcsInterpreter#getCommitFilesForRequirementID(java.lang.String)
	 * @author Gayathery
	 */
	public Iterator<CommitFile> getCommitFilesForRequirementID(String requirementID)
	{
		HashSet<CommitFile> commitFilesList = new HashSet<CommitFile>();
		
			Iterator<String> commits = vcsController.getCommitList();
			CommitMessageParser commitMessageparser = new GitCommitMessageParser();
			while(commits.hasNext())
			{
				String currentCommit = commits.next();
				if(commitMessageparser.parse(vcsController.getCommitMessage(currentCommit)).contains(Integer.valueOf(requirementID)))
				{
					Iterator<CommitFile> commitFilesForAComit = vcsController.getCommitFiles(currentCommit);
					while(commitFilesForAComit.hasNext())
					{
						commitFilesList.add(commitFilesForAComit.next());
					}
					
				}
			}
		
		
		return commitFilesList.iterator();
	}
}
