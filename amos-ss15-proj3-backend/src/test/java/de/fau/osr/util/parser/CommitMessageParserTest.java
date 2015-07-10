/*
 * This file is part of ReqTracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * ReqTracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ReqTracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with ReqTracker.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fau.osr.util.parser;

import de.fau.osr.PublicTestData;
import de.fau.osr.core.vcs.base.Commit;
import de.fau.osr.util.AppProperties;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Taleh Didover on 17.04.15.
 */
@RunWith(Parameterized.class)
public class CommitMessageParserTest extends TestCase {
    private static PublicTestData testData = new PublicTestData();
    private Commit expectedCommit;

    /**
     * @return Collection<Object[]> Each Collection Element represents one set of test data required by one test class execution.
     * Each Element itself is an array containing the different parameters. In this paticular case the array contains one Entry:
     * the expected Commit
     */
    @Parameters
    public static Collection<Object[]> data() {
        List<Object[]> parameters = new ArrayList<Object[]>();
        for(Commit commit: testData.getCommits()){
            parameters.add(new Object[] {commit});
        }
        return parameters;
    }
    
    public CommitMessageParserTest(Commit commit) {
        expectedCommit = commit;
    }

    @Test
    public void parseSimpleTest() throws Exception {
        Parser parser = new CommitMessageParser(Pattern.compile("Req-0*(\\d+)"));
        String test_commit = "major bug-fix Req-08 Req-10 Req-15.";
        List<String> got = parser.parse(test_commit);
        List<String> expected = Arrays.asList("8", "10", "15");
        assertEquals(expected, got);

    }

	@Test
	public void parseMultiplePatternTest() throws Exception {
		Parser parser = new CommitMessageParser(Pattern.compile("Req-0*(\\d+)|Req-(\\d+)|rq:([A-Fa-f0-9]+)"));
		String test_commit = "major bug-fix Req-015 Req-7 rq:0bb486199529 rq:a65d7420.";
		List<String> got = parser.parse(test_commit);
		List<String> expected = Arrays.asList("15", "7", "0bb486199529", "a65d7420");
		assertEquals(expected, got);
	}

	@Test
	public void parseWithDefaultPattern() {
		Parser parser = new CommitMessageParser(Pattern.compile(AppProperties.GetValue("RequirementPattern")));
		String test_commit = "major bug-fix Req-08 Req-10 Req-15.";
		List<String> got = parser.parse(test_commit);
		List<String> expected = Arrays.asList("8", "10", "15");
		assertEquals(expected, got);
	}

    @Test
    public void parseAdvancedTest() {
        Parser parser = new CommitMessageParser(Pattern.compile(AppProperties.GetValue("RequirementPattern")));
        List<String> actual = parser.parse(expectedCommit.getMessage());
        assertTrue(actual.containsAll(expectedCommit.getRequirements()) && expectedCommit.getRequirements().containsAll(actual));
    }
}