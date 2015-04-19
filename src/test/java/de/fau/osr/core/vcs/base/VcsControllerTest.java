/**
 * 
 */
package de.fau.osr.core.vcs.base;

import static org.junit.Assert.*;

import java.util.Iterator;

import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.VcsController;
import de.fau.osr.core.vcs.base.VcsEnvironment;
import org.junit.Test;

/**
 * @author Gayathery
 *
 */
public class VcsControllerTest {
	
    VcsController controller = new VcsController(VcsEnvironment.GIT);
    String uri = "C:\\Users\\Gayathery\\git\\amos\\.git";
	boolean isConnected = false;
    
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

}
