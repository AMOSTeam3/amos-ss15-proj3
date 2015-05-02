package de.fau.osr.core.db;

import com.beust.jcommander.internal.Lists;
import org.apache.commons.collections.IteratorUtils;
import junit.framework.TestCase;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.*;

/**
 * @author: Taleh Didover
 */
public class CSVFileReqCommitRelationDBTest extends TestCase {

    final Path TEST_FILEPATH = Paths.get("src/test/resources/test_req_commit_dependencies.csv");
    ReqCommitRelationDB reqCommitRel;

    public void setUp() throws Exception {
        super.setUp();
        reqCommitRel =  new CSVFileReqCommitRelationDB(TEST_FILEPATH);

        reqCommitRel.addFurtherDependency(5, "dee896c8d52af6bc0b00982ad2fcfca2d9d003dc");
        reqCommitRel.addFurtherDependency(6, "dee896c8d52af6bc0b00982ad2fcfca2d9d003dc");
        reqCommitRel.addFurtherDependency(7, "dee896c8d52af6bc0b00982ad2fcfca2d9d003dc");


        reqCommitRel.addFurtherDependency(10, new HashSet<String>( Arrays.asList(
                new String[]{"b0b5d16e8071c775bdcd1b2d0b1cca464917780b", "a8dc4129802939d620ce0bd3484a1f0538338a0e"})));

    }

    public void tearDown() throws Exception {
        TEST_FILEPATH.toFile().deleteOnExit();
    }

    public void testGetDependenciesOfReqID() throws Exception {
        Collection<String> acutalCommitIDs = IteratorUtils.toList(reqCommitRel.getDependencies(5).iterator());
        Collection<String> expectedCommitIDs = Arrays.asList(new String[]{"dee896c8d52af6bc0b00982ad2fcfca2d9d003dc"});

        assertTrue(acutalCommitIDs.containsAll(expectedCommitIDs) && expectedCommitIDs.containsAll(acutalCommitIDs));
    }

    public void testGetDependenciesOfCommitID() throws Exception {
        Collection<Integer> acutalReqIDs = IteratorUtils.toList(
                reqCommitRel.getDependencies("dee896c8d52af6bc0b00982ad2fcfca2d9d003dc").iterator());
        Collection<Integer> expectedReqIDs = Arrays.asList(new Integer[]{5,6,7});



        assertTrue(acutalReqIDs.containsAll(expectedReqIDs) && expectedReqIDs.containsAll(acutalReqIDs));
    }
}