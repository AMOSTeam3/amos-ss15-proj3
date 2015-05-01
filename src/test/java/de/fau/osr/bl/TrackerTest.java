package de.fau.osr.bl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
public class TrackerTest {
	
	
	VcsController vcs = new VcsController(VcsEnvironment.GIT);
	Tracker interpreter = new Tracker(vcs);

	@Before
	public void setup() {
		vcs.Connect(PublicTestData.getGitTestRepo());
	}
	
	/**
	 * Test method for {@link de.fau.osr.bl.Tracker#getCommitFilesForRequirementID(java.lang.String)}.
	 */
	@Test
	public void testGetCommitFilesForRequirementID() {		
		
			Iterator<CommitFile> commitFileList = interpreter.getCommitFilesForRequirementID(PublicTestData.getSampleReqID()).iterator();	
			assertNotNull(commitFileList);
			assertTrue(commitFileList.hasNext());
		
	}
	
	/**
	 * Test method for {@link de.fau.osr.bl.Tracker#getCommitFilesForRequirementID(java.lang.String)}.
	 */
	@Test
	public void testGetRequirementListforAFile() {		
		
			Iterator<Integer> reqList = interpreter.getAllRequirementsforFile(PublicTestData.getSampleFilePathFromTestRepository()).iterator();	
			boolean isCommitAvailable = false;
			while(reqList.hasNext()){
				if(1 == reqList.next())
					isCommitAvailable = true;
			}
			assertTrue(isCommitAvailable);
		
	}

}
