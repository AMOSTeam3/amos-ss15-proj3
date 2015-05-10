package de.fau.osr.util.parser;

import java.util.List;
import java.util.regex.Pattern;

public interface Parser {

	List<Integer> parse(String latestCommitMessage);
	
	List<Integer> parse(String commitMessage, Pattern pattern);

}
