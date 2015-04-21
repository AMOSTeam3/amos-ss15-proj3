/**
 * 
 */
package de.fau.osr.core.vcs.base;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.fau.osr.PrivateTestData;
import de.fau.osr.parser.CommitMessageParser;
import de.fau.osr.parser.GitCommitMessageParser;

/**
 * @author Florian Gerdes
 *
 */
@RunWith(Parameterized.class)
public class VcsControllerCommitMessageParserTest {
	
    VcsController controller = new VcsController(VcsEnvironment.GIT);
	boolean isConnected = false;
	String uri = PrivateTestData.getGitTestRepo();
	
	@Before
	public void setUpMethods(){
		isConnected = controller.Connect(uri);
	}
	
	@Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                 {"bc87c2039d1e14d5fa0131d77780eaa3b2cc627c", "Creating first TestFile: Req 1", new ArrayList<Integer>()}, 
                 {"4a486acd6261cdc9876c5cb6b6d0e88883eea28d", "3Creating second TestFile: Req 1,4 Req 2.5", new ArrayList<Integer>()}, 
                 {"a8dc4129802939d620ce0bd3484a1f0538338a0e", "Creating third TestFile: Req \"1' 0.07001",new ArrayList<Integer>()},
                 {"b0b5d16e8071c775bdcd1b2d0b1cca464917780b", "Creating fourth TestFile: Req-1",new ArrayList<Integer>(Arrays.asList(1))},
                 {"f3196114a214a91ae3994b6cf6424d8347b2e918", "Req-0.5Update TestReq-6file2",new ArrayList<Integer>(Arrays.asList(0 , 6))},
                 {"dee896c8d52af6bc0b00982ad2fcfca2d9d003dc", "UpdatReq-11eReq--1",new ArrayList<Integer>(Arrays.asList(11))}
           });
    }
    
    private String id;
    private String message;
    private List<Integer> requirements;
    
    public VcsControllerCommitMessageParserTest(String id, String message, List<Integer> requirements) {
		this.id = id;
		this. message = message;
		this.requirements = requirements;
	}
	
    @Test
	public void testConnectString() {
		assertTrue(isConnected);
	}
	
	@Test
	public void testGetCommitMessage(){
		String actual = null;
		if(isConnected){
			actual = controller.getCommitMessage(id);
		}
		assertNotNull(actual);
		assertTrue(message.equals(actual));
	}
	
	@Test
	public void testParse() {
		CommitMessageParser parser = new GitCommitMessageParser();
		List<Integer> actual = parser.parse(message);
		assertTrue(actual.containsAll(requirements) && requirements.containsAll(actual));
	}
}
