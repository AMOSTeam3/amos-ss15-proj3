package de.fau.osr;

import java.util.List;

public class Commit {
	public List<Integer> requirements;
	public String id;
	public String message;
	
	public Commit(String id, String message, List<Integer> requirements) {
		this.id = id;
		this.message = message;
		this.requirements = requirements;
	}

}
