/**
 * 
 */
package de.fau.osr.app;

import de.fau.osr.PublicTestData;
import de.fau.osr.core.vcs.base.Commit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Florian Gerdes
 */
public class CommitRequirementsListAppTest {
    /**
     * outContent: The Outputstream for our tested Class CommitFileListingApp is redirected to this ByteArrayOutputStream; so that we can compare.
     * testData: To get direct access to the commits in the test data repository. Please note, that the commits are created from the related CSVFile
     * not from the repository itself.
     */
    private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static PublicTestData testData = new PublicTestData();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void mainTest(){
        CommitRequirementsListApp.main(new String [] {"-repo", PublicTestData.getGitTestRepo()});
        String expected = buildOutputString(testData.getCommitsWithReqIds());
        assertEquals(outContent.toString(), expected);
    }

    private String buildOutputString(List<Commit> commits){
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(result);
        for(Commit commit: commits){
            for(String i: commit.requirements){
                stream.println("commit " + commit.id + " references Req-" + i);
            }
        }
        return result.toString();
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
    }
}
