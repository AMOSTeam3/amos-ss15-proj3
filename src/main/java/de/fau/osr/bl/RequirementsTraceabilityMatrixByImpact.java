package de.fau.osr.bl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import de.fau.osr.core.vcs.base.CommitFile;
/**
 * This class represents a traceability matrix for requirements based on impact
 * @author Gayathery Sathya
 */
public class RequirementsTraceabilityMatrixByImpact {

	List<String> requirements;
	List<String> files;
	Tracker tracker;
	public static int processProgress = 0;
	Map<RequirementFilePair,RequirementFileImpactValue> requirementTraceabilityByImpactMatrix = new HashMap<RequirementFilePair, RequirementFileImpactValue>();
	public RequirementsTraceabilityMatrixByImpact(Tracker tracker){
		this.tracker = tracker;
		requirements = new ArrayList<String>();
		files = new ArrayList<String>();
	}
	/**
	 * method to process the data behind the generation of traceability matrix
	 */
	public void Process(){
		try {
			requirements.addAll(tracker.getAllRequirements());
			java.util.Collections.sort(requirements);
			files.addAll(tracker.getAllFilesAsString());
			java.util.Collections.sort(files,new FileComparator());
			int progressMaxSize = requirements.size();
			int progressCount = 0;
			for(String requirement : requirements){
				progressCount++;
				processProgress = (progressCount*100)/progressMaxSize;
				Collection<CommitFile> requirementCommitFiles = tracker.getCommitFilesForRequirementID(requirement);
				for(CommitFile requirementCommitFile : requirementCommitFiles){
					/*RequirementFilePair reqFilePair = new RequirementFilePair(requirement,requirementCommitFile.newPath.getPath());
					if(requirementTraceabilityByImpactMatrix.containsKey(reqFilePair)){
						requirementTraceabilityByImpactMatrix.put(reqFilePair,new RequirementFileImpactValue((float)requirementCommitFile.impact + requirementTraceabilityByImpactMatrix.get(reqFilePair).getImpactPercentage()));
						continue;
					}*/
					requirementTraceabilityByImpactMatrix.put(new RequirementFilePair(requirement,requirementCommitFile.newPath.getPath()), new RequirementFileImpactValue((float)requirementCommitFile.impact));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * method to process the data behind the generation of traceability matrix (alternate way)
	 */
	public void ProcessAlternate(){
		try {
			requirements.addAll(tracker.getAllRequirements());
			files.addAll(tracker.getAllFilesAsString());
			for(String requirement: requirements){
				System.out.println(requirement);
				for(String file : files){
					String unixFormatedFilePath = file.replaceAll(Matcher.quoteReplacement("\\"), "/");
					if(!tracker.getAllRequirementsForFile(unixFormatedFilePath).contains(requirement)){
						continue;
					}
					requirementTraceabilityByImpactMatrix.put(new RequirementFilePair(requirement,file), new RequirementFileImpactValue(tracker.getImpactPercentageForFileAndRequirement(unixFormatedFilePath, requirement)));
					
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * @return returns the list of requirements
	 */
	public List<String> getRequirements(){
		return requirements;
	}
	
	/**
	 * @return returns the list of files
	 */
	public List<String> getFiles(){
		return files;
	}
	/**
	 * @param reqFilePair A pair object containing requirement and file
	 * @return returns the impact value for a requirement file pair
	 */
	public RequirementFileImpactValue getImpactValue(RequirementFilePair reqFilePair){
		return requirementTraceabilityByImpactMatrix.get(reqFilePair);
	}
	
	
}

class FileComparator implements Comparator<String>{
	 
	@Override
	public int compare(String s1, String s2) {
		return (new File(s1).getName()).compareTo(new File(s2).getName());
	}
}
