package de.fau.osr.parser;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

/**
 * Created by Taleh Didover on 17.04.15.
 */
public class GitCommitMessageParserTest extends TestCase {

    public void testParse() throws Exception {

        String test_commit = "major bug-fix Req-10 Req-15.";
        CommitMessageParser parser = new GitCommitMessageParser();
		List<Integer> got = parser.parse(test_commit);
        List<Integer> expected = Arrays.asList(10, 15);

        assertEquals(expected, got);

    }
}