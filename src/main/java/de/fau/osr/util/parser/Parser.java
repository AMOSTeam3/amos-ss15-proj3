package de.fau.osr.util.parser;

import java.util.List;

public interface Parser {

    List<String> parse(String latestCommitMessage);

}
