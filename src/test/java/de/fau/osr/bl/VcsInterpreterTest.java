package de.fau.osr.bl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.fau.osr.PublicTestData;
import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.VcsEnvironment;

/**
 * @author Gayathery
 *
 */
public class VcsInterpreterTest {   
    String uri = PublicTestData.getGitTestRepo();
    VcsInterpreter interpreter = new VcsInterpreter(VcsEnvironment.GIT,uri);

	
	/**
	 * Test method for {@link de.fau.osr.bl.VcsInterpreter#getCommitFilesForRequirementID(java.lang.String)}.
	 */
	@Test
	public void testGetCommitFilesForRequirementID() {		
		
			Iterator<CommitFile> commitFileList = interpreter.getCommitFilesForRequirementID(PublicTestData.getSampleReqID());	
			assertNotNull(commitFileList);
			assertTrue(commitFileList.hasNext());
		
	}

}
