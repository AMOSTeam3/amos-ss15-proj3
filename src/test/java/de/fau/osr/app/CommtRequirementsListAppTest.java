/**
 * 
 */
package de.fau.osr.app;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fau.osr.Commit;
import de.fau.osr.PrivateTestData;
import de.fau.osr.PublicTestData;

/**
 * @author Captain Sparrow
 *
 */
public class CommtRequirementsListAppTest {
	private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

	@BeforeClass
	public static void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	    PublicTestData.setUp(PrivateTestData.getTestRepoCsv());
	}
	
	@Test
	public void mainTest(){
		CommitRequirementsListApp.main(new String [] {"-repo", PrivateTestData.getGitTestRepo()});
		assertEquals(outContent.toString(), buildOutputString(PublicTestData.geCommitsWithReqIds()));
	}
	
	private String buildOutputString(List<Commit> commits){
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		PrintStream stream = new PrintStream(result);
		for(Commit commit: commits){
			for(Integer i: commit.requirements){
				stream.println("commit " + commit.id + " references Req-" + i); 
			}
		}
		return result.toString();
		
	}

	@AfterClass
	public static void cleanUpStreams() {
	    System.setOut(null);
	}
}
