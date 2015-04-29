package de.fau.osr.bl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.VcsController;
import de.fau.osr.parser.CommitMessageParser;
import de.fau.osr.parser.GitCommitMessageParser;
/**
 * @author Gayathery
 * @desc This class is an interpreter for data from Vcs
 *
 */
public class VcsInterpreter {

	VcsController vcsController;

	public VcsInterpreter(VcsController vcsController) {
		this.vcsController = vcsController;
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
	
	/* (non-Javadoc)
	 * @see de.fau.osr.bl.VcsInterpreter#getCommitFilesForRequirementID(java.lang.String)
	 * @author Gayathery
	 */
	public List<Integer> getRequirementListforAFile(String filePath){
		ArrayList<Integer> requirementList = new ArrayList<Integer>();
		Iterator<String> commitIdListIterator = vcsController.getCommitIdsForFileodification(filePath);
		CommitMessageParser messageParser = new GitCommitMessageParser();
		while(commitIdListIterator.hasNext()){
			requirementList.addAll(messageParser.parse(vcsController.getCommitMessage(commitIdListIterator.next())));
		}
		return requirementList;
	}
}
