package spicetraceability.view;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.fau.osr.bl.Tracker;
import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.VcsController;

public class DataRetriever {

	Tracker tracker;
	VcsController vcsController;
	
	public DataRetriever(VcsController vcsController,Tracker tracker){
		this.vcsController = vcsController;
		this.tracker = tracker;
	}

		/*
	 * added a parameter 'requirementPattern' to easily change the pattern in runtime
	 * have to ask the author of this method to extend the method in the master
	 */
	public List<Integer> parse(String latestCommitMessage,String requirementPattern) {
		final Pattern REQUIREMENT_PATTERN = Pattern.compile(requirementPattern);
		Matcher m = REQUIREMENT_PATTERN.matcher(latestCommitMessage);
		List<Integer> found_reqids = new ArrayList<Integer>();

		while(m.find())  {
			found_reqids.add(Integer.valueOf(m.group(1)));
		}

		return found_reqids;

	}
	/*
	 * This functionality will be provided in master as a overload for getCommitFilesForRequirementID once the parse extenstion is made in master
	 */
	public ArrayList<CommitFile> getCommitFilesForRequirementID(String requirementID,String requirementPattern)
	{

		ArrayList<CommitFile> commitFilesList = new ArrayList<CommitFile>();
		
			Iterator<String> commits = vcsController.getCommitList();
			
			while(commits.hasNext())
			{
				String currentCommit = commits.next();
				if(parse(vcsController.getCommitMessage(currentCommit),requirementPattern).contains(Integer.valueOf(requirementID)))
				{
					commitFilesList.addAll(vcsController.getCommitFiles(currentCommit));
					
				}
			}
		
		
		return commitFilesList;
	}


	
	
	

}
