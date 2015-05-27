/**
 * 
 */
package de.fau.osr.app;

import de.fau.osr.PublicTestData;
import de.fau.osr.core.vcs.base.Commit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * @author Florian Gerdes
 * @see <a href="URL#https://github.com/uv48uson/amos-ss15-proj3/wiki/3.-Testing">Parameterized Test Classes</a>
 */
@RunWith(Parameterized.class)
public class CommitFileListingAppTest {
	/**
	 * outContent: The Outputstream for our tested Class CommitFileListingApp is redirected to this ByteArrayOutputStream; so that we can compare.
	 * testData: To get direct access to the commits in the test data repository. Please note, that the commits are created from the related CSVFile
	 * not from the repository itself.
	 * expectedCommit: Commit containing the expected results for one run. Note that this is a parameterized Class, so that it is executed multiple
	 * times with different data.
	 */
	private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private static PublicTestData testData = new PublicTestData();
	private Commit expectedCommit;

	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	}
	
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
    
    public CommitFileListingAppTest(Commit commit) {
		expectedCommit = commit;
	}
	
	@Test
	public void mainTest(){
		CommitFileListingApp.main(new String [] {"-repo", PublicTestData.getGitTestRepo(), "-commit", expectedCommit.id});
		String expected = buildOutputString(expectedCommit.id);
		assertEquals(expected, outContent.toString());
	}
	
	private String buildOutputString(String commitId){
		Map<String,String> lookupTable = new HashMap<String,String>();
		lookupTable.put("dee896c8d52af6bc0b00982ad2fcfca2d9d003dc","TestFile3 MODIFIED TestFile3");
		lookupTable.put("f3196114a214a91ae3994b6cf6424d8347b2e918","TestFile2 MODIFIED TestFile2");
		lookupTable.put("b0b5d16e8071c775bdcd1b2d0b1cca464917780b","/dev/null ADDED TestFile4");
		lookupTable.put("bc87c2039d1e14d5fa0131d77780eaa3b2cc627c","/dev/null ADDED TestFile1");
		lookupTable.put("4a486acd6261cdc9876c5cb6b6d0e88883eea28d","/dev/null ADDED TestFile2");
		lookupTable.put("a8dc4129802939d620ce0bd3484a1f0538338a0e","/dev/null ADDED TestFile3");
        String message = lookupTable.get(commitId);
        message = message.replace("/", System.getProperty("file.separator")); //depends on OS
		return message + System.getProperty("line.separator"); //depends on OS
	}

	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	}
}
