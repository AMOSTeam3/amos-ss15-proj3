/*
 * This file is part of Req-Tracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * Req-Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Req-Tracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Req-Tracker.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fau.osr.bl;

import de.fau.osr.PublicTestData;
import de.fau.osr.core.db.DBTestHelper;
import de.fau.osr.core.vcs.base.VcsEnvironment;
import de.fau.osr.core.vcs.interfaces.VcsClient;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class RequirementsTraceabilityMatrixByImpactTest {

    List<String> files;
    static Tracker tracker;
    static RequirementsTraceabilityMatrixByImpact matrix;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        VcsClient client =  VcsClient.connect(VcsEnvironment.GIT, PublicTestData.getGitTestRepo());
        tracker = new Tracker(client, null, null, DBTestHelper.createH2SessionFactory());
        matrix  = new RequirementsTraceabilityMatrixByImpact(tracker);
    }

    @Test
    public void testProcess() {
        matrix.Process();
        Collection files  = tracker.getAllFilesAsString();
        for(Object file : files) {

            RequirementFileImpactValue value = matrix.getImpactValue(new RequirementFilePair("1",file.toString()));
            if(value!=null){
                if(file.toString().equals("TestFile4")){ // && value.impactPercentage==100.0
                    assertTrue(true);
                    return;
                }

            }


        }
        assertTrue(false);

    }


}
