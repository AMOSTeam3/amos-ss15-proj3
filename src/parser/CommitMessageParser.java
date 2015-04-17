package parser;

import java.util.List;

public interface CommitMessageParser {

	List<Integer> parse(String latestCommitMessage);

}
