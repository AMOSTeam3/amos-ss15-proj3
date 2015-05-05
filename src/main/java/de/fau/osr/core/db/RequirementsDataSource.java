package de.fau.osr.core.db;


import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class RequirementsDataSource {

	
	public Set<String> getRequirementIDs(){
		
		Set<String> requirementsList = new HashSet<String>();
		InputStream is = RequirementsDataSource.class.getResourceAsStream("/RequirementIds.txt");
		
		@SuppressWarnings("resource")
		String inputStreamString = new Scanner(is,"UTF-8").useDelimiter("\\A").next();
		String[] ids = inputStreamString.split("\\|");
	       for(String id : ids)
	    	   requirementsList.add(id);
		
	     return requirementsList;

	}

}
