package parser;

import java.util.List;

public interface Parser {

	List<Integer> parse(String latestCommitMessage);

}
