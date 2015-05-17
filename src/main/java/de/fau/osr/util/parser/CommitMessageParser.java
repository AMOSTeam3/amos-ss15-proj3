package de.fau.osr.util.parser;

import de.fau.osr.util.AppProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Taleh Didover
 *
 * Parses requirement id in given commit message.
 * Format of requirement ids must be Req-\d+
 *
 */
public class CommitMessageParser implements Parser {

	private Pattern reqPattern;

    public CommitMessageParser(Pattern pattern) {
        if (pattern != null) {
            this.setPattern(pattern);
        }
    }

	@Override
	public List<String> parse(String latestCommitMessage) {
		Matcher m = reqPattern.matcher(latestCommitMessage);
		List<String> found_reqids = new ArrayList<String>();

		while(m.find())  {
			found_reqids.add(m.group(1));
		}

		return found_reqids;

	}

	public void setPattern(Pattern pattern) {
		reqPattern = pattern;
	}
	
	public Pattern getPattern() {
		return reqPattern;
	}
}
