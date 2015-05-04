package de.fau.osr.util.parser;

import java.util.List;

public interface Parser {

	List<Integer> parse(String latestCommitMessage);

}
