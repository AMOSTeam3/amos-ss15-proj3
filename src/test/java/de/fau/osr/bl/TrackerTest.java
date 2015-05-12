package de.fau.osr.bl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSetMultimap;
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
import java.util.Map;

import static org.junit.Assert.*;
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
		
			Iterator<CommitFile> commitFileList = interpreter.getCommitFilesForRequirementID(PublicTestData.getSampleReqID()).iterator();	
			assertNotNull(commitFileList);
			assertTrue(commitFileList.hasNext());
		
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
        //stub getAllReqCommitRelationsFromVcs
        ImmutableSetMultimap.Builder<String, String> vcsReqBuilder = ImmutableSetMultimap.builder();
        vcsReqBuilder.put("1", "commit1");
        vcsReqBuilder.put("1", "commit2");
        vcsReqBuilder.put("1", "commit20");
        vcsReqBuilder.put("2", "commit30");
        vcsReqBuilder.put("3", "commit30");
        vcsReqBuilder.put("4", "commit40");
        ImmutableSetMultimap<String, String> vcsReq = vcsReqBuilder.build();
        Mockito.doReturn(vcsReq).when(tracker).getAllReqCommitRelationsFromVcs();

        //when
        SetMultimap<String, String> reqCommitLinkage = tracker.getAllReqCommitRelations();

        //than
        SetMultimap<String, String> expected = HashMultimap.create();
        expected.putAll(dbReqs);
        expected.putAll(vcsReq);

        for (Map.Entry<String, String> entry : expected.entries()){
            assertTrue(reqCommitLinkage.containsEntry(entry.getKey(), entry.getValue()));
        }
        assertEquals(reqCommitLinkage.size(), expected.size());
    }

    @Test
    public void getAllReqCommitRelationsFromVcsTest() throws Exception {
        SetMultimap<String, String> result = interpreter.getAllReqCommitRelationsFromVcs();
        SetMultimap<String, String> expected = HashMultimap.create();
        expected.put("0","f3196114a214a91ae3994b6cf6424d8347b2e918");
        expected.put("1","b0b5d16e8071c775bdcd1b2d0b1cca464917780b");
        expected.put("6","f3196114a214a91ae3994b6cf6424d8347b2e918");
        expected.put("11","dee896c8d52af6bc0b00982ad2fcfca2d9d003dc");
        assertEquals(result, expected);

    }
}
