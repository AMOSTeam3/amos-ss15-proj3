/*
 * This file is part of ReqTracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * ReqTracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ReqTracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with ReqTracker.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fau.osr.bl;

import de.fau.osr.PublicTestData;
import de.fau.osr.core.db.DBTestHelper;
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
        tracker = new Tracker(client, null, null, DBTestHelper.createH2SessionFactory());
        requirements = tracker.getRequirementIds();
        matrix  = new RequirementsTraceabilityMatrix(requirements);

    }

    @Test
    public void testPopulateMatrix() {
        List<String> fileRequirements;
        String unixFormatedFilePath = "TestFile2";
        try {
            fileRequirements = new ArrayList<String>( tracker.getRequirementIdsForFile(unixFormatedFilePath));
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
