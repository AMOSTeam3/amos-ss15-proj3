package de.fau.osr.bl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import de.fau.osr.PublicTestData;
import de.fau.osr.core.db.DataSource;
import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.VcsEnvironment;
import de.fau.osr.core.vcs.interfaces.VcsClient;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * @author Gayathery
 *
 */
public class TrackerTest {
	
    static VcsClient client;
    static Tracker interpreter;


	@BeforeClass
	public static void prepare() throws IOException {
        client =  VcsClient.connect(VcsEnvironment.GIT, PublicTestData.getGitTestRepo());
		interpreter = new Tracker(client);
	}

	/**
	 * Test method for {@link de.fau.osr.bl.Tracker#getCommitFilesForRequirementID(java.lang.String)}.
	 */
	@Test
	public void testGetCommitFilesForRequirementID() throws IOException {
        //refault pattern Req-(\\d+)
        List<CommitFile> commitFileList = interpreter.getCommitFilesForRequirementID("1");
        assertTrue(commitFileList.size() == 1);
        assertEquals(commitFileList.get(0).newPath.getName(), "TestFile4");

        //test another pattern, should contain 2 files
        interpreter = new Tracker(client, null, null, Pattern.compile("Req\\s(\\d+)"));
        commitFileList = interpreter.getCommitFilesForRequirementID("1");
        assertTrue(commitFileList.size() == 2);
        assertEquals(commitFileList.get(0).newPath.getName(), "TestFile2");
        assertEquals(commitFileList.get(1).newPath.getName(), "TestFile1");

	}
	
	/**
	 * Test method for {@link de.fau.osr.bl.Tracker#getCommitFilesForRequirementID(java.lang.String)}.
	 */
	@Test
	public void testGetRequirementListforAFile() throws IOException {
		
			Iterator<String> reqList = interpreter.getAllRequirementsForFile(PublicTestData.getSampleFilePathFromTestRepository()).iterator();
			boolean isCommitAvailable = false;
			while(reqList.hasNext()){
				if("1".equals(reqList.next()))
					isCommitAvailable = true;
			}
			assertTrue(isCommitAvailable);
		
	}

    @Test
    public void getTotalReqLinkageTest() throws Exception {
        //given
        VcsClient mockedClient = mock(VcsClient.class);
        DataSource mockedSource = mock(DataSource.class);
        Tracker tracker = Mockito.spy(new Tracker(mockedClient, mockedSource, null, null));
        //stub for dataSource.getAllReqCommitRelations()
        SetMultimap<String, String> dbReqs = HashMultimap.create();
        dbReqs.put("1","commit1");
        dbReqs.put("1","commit2");
        dbReqs.put("2","commit3");
        dbReqs.put("3", "commit3");
        dbReqs.put("4", "commit4");

        Mockito.doReturn(dbReqs).when(mockedSource).getAllReqCommitRelations();

        //when
        SetMultimap<String, String> reqCommitLinkage = tracker.getAllReqCommitRelations();

        //than
        SetMultimap<String, String> expected = HashMultimap.create();
        expected.putAll(dbReqs);

        for (Map.Entry<String, String> entry : expected.entries()){
            assertTrue(reqCommitLinkage.containsEntry(entry.getKey(), entry.getValue()));
        }
        assertEquals(reqCommitLinkage.size(), expected.size());
    }

}
