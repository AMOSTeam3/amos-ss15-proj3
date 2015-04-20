/**
 * 
 */
package de.fau.osr.parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.fau.osr.util.AppProperties;


/**
 * @author Captain Sparrow, Taleh Didover
 *
 * Parses requirement id in given commit message.
 * Format of requirement ids must be Req-\d+
 *
 */
public class GitCommitMessageParser implements CommitMessageParser {

	private static final Pattern REQUIREMENT_PATTERN = Pattern.compile(AppProperties.GetValue("RequirementPattern"));

	@Override
	public List<Integer> parse(String latestCommitMessage) {
		Matcher m = REQUIREMENT_PATTERN.matcher(latestCommitMessage);
		List<Integer> found_reqids = new ArrayList<Integer>();

		while(m.find()) {
			found_reqids.add(Integer.valueOf(m.group(1)));
		}

		return found_reqids;
		Properties properties = new Properties();
		try {
		  properties.load(new FileInputStream("path/filename"));
		} catch (IOException e) {
		  
		}
	}
}
