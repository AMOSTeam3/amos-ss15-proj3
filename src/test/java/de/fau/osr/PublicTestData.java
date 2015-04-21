package de.fau.osr;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PublicTestData {
	public static List<Commit> commits  = new ArrayList<Commit>();
	
	public static void setUp(String csvFilePath){
		parseCsvFile(csvFilePath);
	}
	
	private static void parseCsvFile(String filePath){
		BufferedReader br = null;
		String csvSplitStringsBy = ";";
		String csvSplitEntriesBy = ",";
	 
		try {
	 
			br = new BufferedReader(new FileReader(filePath));
			
			String line = "";
			while ((line = br.readLine()) != null) {
				String[] commitString = line.split(csvSplitStringsBy);
				List<Integer> requirements = new ArrayList<Integer>();
				if(commitString.length >= 3){
					String [] requirementStrings = commitString[2].split(csvSplitEntriesBy);
					for(String str: requirementStrings){
						requirements.add(Integer.parseInt(str));
					}
				}
				
				Commit commit = new Commit(commitString[0], commitString[1], requirements);
				commits.add(commit);
			}
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static List<Commit> geCommitsWithReqIds() {
		List<Commit> commitsWithReqIds = new ArrayList<Commit>();
		for(Commit commit: commits){
			if(commit.requirements.size() > 0){
				commitsWithReqIds.add(commit);
			}
		}
		return commitsWithReqIds;
	}

}
