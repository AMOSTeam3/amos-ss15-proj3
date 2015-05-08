package de.fau.osr.gui;

import java.util.ArrayList;
import java.util.List;

import de.fau.osr.core.vcs.base.Commit;
import de.fau.osr.core.vcs.base.CommitFile;

public class DataTransformer {

	public static String[] getMessagesFromCommits(ArrayList<Commit> commits){
			String[] commitMessagesArray = new String[commits.size()];
			for(int i = 0; i<commits.size(); i++){
				commitMessagesArray[i] = commits.get(i).message;
			}
			return commitMessagesArray;
	}

	public static String[] getNamesFromCommitFiles(List<CommitFile> commitFiles){
			String[] commitfilesArray = new String[commitFiles.size()];
			for (int i = 0; i < commitFiles.size(); i++) {
				commitfilesArray[i] = commitFiles.get(i).oldPath + " "
						+ commitFiles.get(i).commitState + " "
						+ commitFiles.get(i).newPath;
			}
			return commitfilesArray;
	}

}
