package de.fau.osr.bl;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Gayathery Sathya
 * @desc This class represents the relationship between requirements
 */
public class RequirementsRelation {
	
	String requirementX;
	String requirementY;
	Integer commonFilesCount = 0;
	List<String> commonFiles = new ArrayList<String>();
	
	
	public RequirementsRelation() {
		super();
		
	}


	public RequirementsRelation(String requirementX, String requirementY,
			List<String> files) {
		super();
		this.requirementX = requirementX;
		this.requirementY = requirementY;
		this.commonFiles = files;
	}


	public RequirementsRelation(String requirementX, String requirementY,
			Integer commonFilesCount) {
		super();
		this.requirementX = requirementX;
		this.requirementY = requirementY;
		this.commonFilesCount = commonFilesCount;
	}


	public String getRequirementX() {
		return requirementX;
	}


	public void setRequirementX(String requirementX) {
		this.requirementX = requirementX;
	}


	public String getRequirementY() {
		return requirementY;
	}


	public void setRequirementY(String requirementY) {
		this.requirementY = requirementY;
	}


	public Integer getCommonFilesCount() {
		return commonFilesCount;
	}


	public void setCommonFilesCount(Integer commonFilesCount) {
		commonFilesCount = commonFilesCount;
	}


	public List<String> getFiles() {
		return commonFiles;
	}


	public void setFiles(List<String> files) {
		this.commonFiles = files;
	}
	
	public void incrementRelations(int commonFilesCount, List<String> commonFiles)	{
		this.commonFilesCount += commonFilesCount;
		if(commonFiles != null)
			this.commonFiles.addAll(commonFiles);
	}
	
	public void decrementRelations(int commonFilesCount, List<String> commonFiles)	{
		this.commonFilesCount -= commonFilesCount;
		if(commonFiles != null)
			this.commonFiles.removeAll(commonFiles);
	}

}
