package de.fau.osr.util.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.fau.osr.PublicTestData;
import de.fau.osr.core.vcs.base.Commit;

/**
 * Created by Taleh Didover on 17.04.15.
 */
@RunWith(Parameterized.class)
public class CommitMessageParserTest extends TestCase {
	private static PublicTestData testData = new PublicTestData();
	private Commit expectedCommit;

	/*
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
    public void parseSimpleTest1() throws Exception {

        String test_commit = "major bug-fix Req-10 Req-15.";
        Parser parser = new CommitMessageParser();
		List<Integer> got = parser.parse(test_commit);
        List<Integer> expected = Arrays.asList(10, 15);

        assertEquals(expected, got);

    }
	
	@Test
    public void parseSimpleTest2() throws Exception {

        String test_commit = "major bug-fix Req-10 Req-15.";
        Pattern pattern = Pattern.compile("Req-(\\d+)");
        Parser parser = new CommitMessageParser();
		List<Integer> got = parser.parse(test_commit, pattern);
        List<Integer> expected = Arrays.asList(10, 15);

        assertEquals(expected, got);

    }
    
    @Test
	public void parseAdvancedTest1() {
		Parser parser = new CommitMessageParser();
		List<Integer> actual = parser.parse(expectedCommit.message);
		assertTrue(actual.containsAll(expectedCommit.requirements) && expectedCommit.requirements.containsAll(actual));
	}
    
    @Test
	public void parseAdvancedTest2() {
		Parser parser = new CommitMessageParser();
		Pattern pattern = Pattern.compile("Req-(\\d+)");
		List<Integer> actual = parser.parse(expectedCommit.message, pattern);
		assertTrue(actual.containsAll(expectedCommit.requirements) && expectedCommit.requirements.containsAll(actual));
	}
}