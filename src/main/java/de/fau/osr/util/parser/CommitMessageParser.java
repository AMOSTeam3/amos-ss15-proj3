/**
 * 
 */
package de.fau.osr.util.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.fau.osr.util.AppProperties;


/**
 * @author Taleh Didover
 *
 * Parses requirement id in given commit message.
 * Format of requirement ids must be Req-\d+
 *
 */
public class CommitMessageParser implements Parser {

	private static Pattern REQUIREMENT_PATTERN = Pattern.compile(AppProperties.GetValue("RequirementPattern"));

	@Override
	public List<Integer> parse(String latestCommitMessage) {
		Matcher m = REQUIREMENT_PATTERN.matcher(latestCommitMessage);
		List<Integer> found_reqids = new ArrayList<Integer>();

		while(m.find())  {
			found_reqids.add(Integer.valueOf(m.group(1)));
		}

		return found_reqids;

	}

	public static void setPattern(Pattern pattern) {
		REQUIREMENT_PATTERN = pattern;
	}
	
	public static Pattern getPattern() {
		return REQUIREMENT_PATTERN;
	}
}
