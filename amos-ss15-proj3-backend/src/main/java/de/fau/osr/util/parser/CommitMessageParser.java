package de.fau.osr.util.parser;

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

        while (m.find())
            for (int i=0; i < m.groupCount(); ++i) {
                String group = m.group(i + 1);
                if (group != null)
                    found_reqids.add(group);
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