package de.fau.osr.core.vcs.base;

import java.util.List;

/**
 * This Class is a Container within the test framework for all information related to one commit.
 * @author: Florian Gerdes
 */
public class Commit {
	public List<String> requirements;
	public String id;
	public String message;
	public List<CommitFile> files;
	
	public Commit(String id, String message, List<String> requirements, List<CommitFile> files) {
		this.id = id;
		this.message = message;
		this.requirements = requirements;
		this.files = files;
	}

}
