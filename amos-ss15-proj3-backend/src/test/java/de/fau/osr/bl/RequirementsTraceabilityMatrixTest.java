package de.fau.osr.bl;

import de.fau.osr.PublicTestData;
import de.fau.osr.core.vcs.base.VcsEnvironment;
import de.fau.osr.core.vcs.interfaces.VcsClient;
import de.fau.osr.util.matrix.MatrixIndex;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class RequirementsTraceabilityMatrixTest {

    static Collection<String> requirements;
    static Tracker tracker;
    static RequirementsTraceabilityMatrix matrix;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        VcsClient client =  VcsClient.connect(VcsEnvironment.GIT, PublicTestData.getGitTestRepo());
        tracker = new Tracker(client, null, null);
        requirements = tracker.getAllRequirements();
        matrix  = new RequirementsTraceabilityMatrix(requirements);

    }

    @Test
    public void testPopulateMatrix() {
        List<String> fileRequirements;
        String unixFormatedFilePath = "TestFile2";
        try {
            fileRequirements = new ArrayList<String>( tracker.getAllRequirementsForFile(unixFormatedFilePath));
            if(fileRequirements !=null && !fileRequirements.isEmpty())
                matrix.populateMatrix(fileRequirements,unixFormatedFilePath);
            RequirementsRelation reqRelation = matrix.traceabilityMatrix.getAt(new MatrixIndex(0,1));
                assertTrue(reqRelation == null);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
