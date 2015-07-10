/*
 * This file is part of ReqTracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * ReqTracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ReqTracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with ReqTracker.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fau.osr.bl;

import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.util.ProgressBarInterface;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
/**
 * This class represents a traceability matrix for requirements based on impact
 * @author Gayathery Sathya
 */
public class RequirementsTraceabilityMatrixByImpact {

    List<String> requirements;
    List<String> files;
    Tracker tracker;
    private int processProgress = 0;
    private ProgressBarInterface progressBar;
    Map<RequirementFilePair,RequirementFileImpactValue> requirementTraceabilityByImpactMatrix = new HashMap<RequirementFilePair, RequirementFileImpactValue>();
    public RequirementsTraceabilityMatrixByImpact(Tracker tracker){
        this.tracker = tracker;
        requirements = new ArrayList<String>();
        files = new ArrayList<String>();
        processProgress = 0;
    }
    /**
     * method to process the data behind the generation of traceability matrix
     */
    public void Process(){
        try {
            requirements.addAll(tracker.getRequirementIds());
            java.util.Collections.sort(requirements);
            files.addAll(tracker.getAllFilesAsString());
            java.util.Collections.sort(files,new FileComparator());
            int progressMaxSize = requirements.size();
            int progressCount = 0;
            for(String requirement : requirements){
                progressCount++;
                int nextProcessProgress = (progressCount*100)/progressMaxSize;
                if(nextProcessProgress != processProgress && progressBar != null) {
                	progressBar.setProgressBarValue(nextProcessProgress);
                }
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
            requirements.addAll(tracker.getRequirementIds());
            files.addAll(tracker.getAllFilesAsString());
            for(String requirement: requirements){
                System.out.println(requirement);
                for(String file : files){
                    String unixFormatedFilePath = file.replaceAll(Matcher.quoteReplacement("\\"), "/");
                    if(!tracker.getRequirementIdsForFile(unixFormatedFilePath).contains(requirement)){
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

    public String getRepositoryName() {
        return  tracker.getRepositoryName();
    }
	/**
	 * @param progressBar the progressBar to set
	 */
	public void setProgressBar(ProgressBarInterface progressBar) {
		this.progressBar = progressBar;
	}

}

class FileComparator implements Comparator<String>{

    @Override
    public int compare(String s1, String s2) {
        return (new File(s1).getName()).compareTo(new File(s2).getName());
    }
}
