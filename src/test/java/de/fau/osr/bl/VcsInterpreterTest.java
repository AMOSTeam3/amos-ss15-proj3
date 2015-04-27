package de.fau.osr.bl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.fau.osr.PublicTestData;
import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.VcsController;
import de.fau.osr.core.vcs.base.VcsEnvironment;

/**
 * @author Gayathery
 *
 */
public class VcsInterpreterTest {
	VcsController vcs = new VcsController(VcsEnvironment.GIT);
	VcsInterpreter interpreter = new VcsInterpreter(vcs);

	@Before
	public void setup() {
		vcs.Connect(PublicTestData.getGitTestRepo());
	}
	
	/**
	 * Test method for {@link de.fau.osr.bl.VcsInterpreter#getCommitFilesForRequirementID(java.lang.String)}.
	 */
	@Test
	public void testGetCommitFilesForRequirementID() {		
		
			Iterator<File> commitFileList = interpreter.getCommitFilesForRequirementID(PublicTestData.getSampleReqID()).iterator();
			assertNotNull(commitFileList);
			assertTrue(commitFileList.hasNext());
		
	}

}
