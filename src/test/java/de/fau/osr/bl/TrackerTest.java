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
import de.fau.osr.core.vcs.base.VcsEnvironment;
import de.fau.osr.core.vcs.interfaces.VcsClient;

/**
 * @author Gayathery
 *
 */
public class TrackerTest {
	
	
	VcsClient client = VcsClient.connect(VcsEnvironment.GIT, PublicTestData.getGitTestRepo());
	Tracker interpreter = new Tracker(client);
	
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
