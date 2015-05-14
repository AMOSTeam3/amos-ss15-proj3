package de.fau.osr.core.db;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import de.fau.osr.PublicTestData;
import de.fau.osr.core.vcs.base.VcsEnvironment;
import de.fau.osr.core.vcs.interfaces.VcsClient;
import de.fau.osr.util.parser.CommitMessageParser;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by Dmitry Gorelenkov on 14.05.2015.
 */
public class VCSDataSourceTest {
    static VcsClient client;
    static VCSDataSource vcsDs;

    @BeforeClass
    public static void prepare() throws IOException {
        client =  VcsClient.connect(VcsEnvironment.GIT, PublicTestData.getGitTestRepo());
        vcsDs = new VCSDataSource(client, new CommitMessageParser());
    }

    @Test(expected = OperationNotSupportedException.class)
    public void addReqCommitRelationTest() throws Exception {
        vcsDs.addReqCommitRelation("1","2");
    }

    @Test(expected = OperationNotSupportedException.class)
    public void removeReqCommitRelationTest() throws Exception {
        vcsDs.removeReqCommitRelation("1","2");
    }

    @Test
    public void getAllReqCommitRelationsTest() throws Exception {
            SetMultimap<String, String> result = vcsDs.getAllReqCommitRelations();
            SetMultimap<String, String> expected = HashMultimap.create();
            expected.put("0","f3196114a214a91ae3994b6cf6424d8347b2e918");
            expected.put("1","b0b5d16e8071c775bdcd1b2d0b1cca464917780b");
            expected.put("6","f3196114a214a91ae3994b6cf6424d8347b2e918");
            expected.put("11","dee896c8d52af6bc0b00982ad2fcfca2d9d003dc");
            assertEquals(result, expected);
    }
}