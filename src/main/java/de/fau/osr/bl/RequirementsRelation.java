package de.fau.osr.bl;

import java.util.List;

public class RequirementsRelation {
	
	String requirementX;
	String requirementY;
	Integer CommonFilesCount = 0;
	List<String> files;
	
	
	public RequirementsRelation() {
		super();
		
	}


	public RequirementsRelation(String requirementX, String requirementY,
			List<String> files) {
		super();
		this.requirementX = requirementX;
		this.requirementY = requirementY;
		this.files = files;
	}


	public RequirementsRelation(String requirementX, String requirementY,
			Integer commonFilesCount) {
		super();
		this.requirementX = requirementX;
		this.requirementY = requirementY;
		CommonFilesCount = commonFilesCount;
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
		return CommonFilesCount;
	}


	public void setCommonFilesCount(Integer commonFilesCount) {
		CommonFilesCount = commonFilesCount;
	}


	public List<String> getFiles() {
		return files;
	}


	public void setFiles(List<String> files) {
		this.files = files;
	}
	
	

}
