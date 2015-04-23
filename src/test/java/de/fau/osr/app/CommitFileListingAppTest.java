/**
 * 
 */
package de.fau.osr.app;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.fau.osr.PublicTestData;
import de.fau.osr.core.vcs.base.Commit;
import de.fau.osr.core.vcs.base.CommitFile;

/**
 * @author Florian Gerdes
 * @see <a href="URL#https://github.com/uv48uson/amos-ss15-proj3/wiki/3.-Testing">Parameterized Test Classes</a>
 */
@RunWith(Parameterized.class)
public class CommitFileListingAppTest {
	/*
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
    
    public CommitFileListingAppTest(Commit commit) {
		expectedCommit = commit;
	}
	
	@Test
	public void mainTest(){
		CommitFileListingApp.main(new String [] {"-repo", PublicTestData.getGitTestRepo(), "-commit", expectedCommit.id});
		List<Commit> commits = testData.getCommits();
		String expected = buildOutputString(commits);
		assertEquals(outContent.toString(), expected);
	}
	
	private String buildOutputString(List<Commit> commits){
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		PrintStream stream = new PrintStream(result);
		for(Commit commit: commits){
			if(commit.id.equals(expectedCommit.id)){
				for(CommitFile file: commit.files){
					stream.println(file.oldPath + " " + file.commitState + " " + file.newPath);
				}
			}
		}
		return result.toString();
	}

	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	}
}
