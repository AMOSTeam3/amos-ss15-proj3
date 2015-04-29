/**
 * 
 */
package de.fau.osr.core.vcs.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.fau.osr.PublicTestData;

/**
 * @author Gayathery
 *
 */
@RunWith(Parameterized.class)
public class VcsControllerTest {
	private static PublicTestData testData = new PublicTestData();
	private Commit expectedCommit;
    VcsController controller = new VcsController(VcsEnvironment.GIT);
    String uri = PublicTestData.getGitTestRepo();
	boolean isConnected = false;
	
	
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
	
	public VcsControllerTest(Commit commit) {
		expectedCommit = commit;
	}
    
	/**
	 * Test method for {@link org.amos.core.vcs.base.VcsController#Connect(java.lang.String)}.
	 */
	@Test
	public void testConnectString() {
		
		isConnected = controller.Connect(uri);
		assertTrue(isConnected);
	}

	/**
	 * Test method for {@link org.amos.core.vcs.base.VcsController#getBranchList()}.
	 */
	@Test
	public void testGetBranchList() {
		isConnected = controller.Connect(uri);
		Iterator<String> branchList = null;
		if(isConnected) 
		  branchList = controller.getBranchList();
		
		assertNotNull(branchList);
		assertTrue(branchList.hasNext());
	}

	/**
	 * Test method for {@link org.amos.core.vcs.base.VcsController#getCommitList()}.
	 */
	@Test
	public void testGetCommitList() {
		isConnected = controller.Connect(uri);
		Iterator<String> commitList = null;
		if(isConnected)
			commitList = controller.getCommitList();
		assertNotNull(commitList);
		assertTrue(commitList.hasNext());
	}

	/**
	 * Test method for {@link org.amos.core.vcs.base.VcsController#getCommitFiles(java.lang.String)}.
	 */
	@Test
	public void testGetCommitFiles() {
		isConnected = controller.Connect(uri);
		Iterator<CommitFile> commitFileList = null;
		Iterator<String> commitList = null;
		if(isConnected){
			commitList = controller.getCommitList();
			if(commitList.hasNext()){
				commitFileList = controller.getCommitFiles(commitList.next());
			}
			  
			
		}
		assertNotNull(commitFileList);
		assertTrue(commitFileList.hasNext());
	}
	
	/**
	 * Test method for {@link org.amos.core.vcs.base.VcsController#getCommitMessage(java.lang.String)}.
	 */
	@Test
	public void getCommitMessageSimpleTest() {
		isConnected = controller.Connect(uri);
		String commitMessage = null;
		Iterator<String> commitList = null;
		if(isConnected){
			commitList = controller.getCommitList();
			if(commitList.hasNext()){
				commitMessage = controller.getCommitMessage(commitList.next());
			}
			  
			
		}
		assertNotNull(commitMessage);
	}

}
