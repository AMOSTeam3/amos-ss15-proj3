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
/**
 * 
 */
package de.fau.osr.core.vcs.base;

import de.fau.osr.PublicTestData;
import de.fau.osr.core.vcs.interfaces.VcsClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Gayathery
 *
 */
@RunWith(Parameterized.class)
public class VcsControllerTest {
    private static PublicTestData testData = new PublicTestData();
    private Commit expectedCommit;
    VcsClient client = VcsClient.connect(VcsEnvironment.GIT, PublicTestData.getGitTestRepo());

    /**
     * @return Collection<Object[]> Each Collection Element represents one set of test data required by one test class execution.
     * Each Element itself is an array containing the different parameters. In this paticular case the array contains one Entry:
     * the expected Commit
     */
    @Parameters
    public static Collection<Object[]> data() {
        List<Object[]> parameters = new ArrayList<Object[]>();
        for(Commit commit: testData.getCommits()){
            parameters.add(new Object[] {commit});
        }
        return parameters;
    }

    public VcsControllerTest(Commit commit) {
        expectedCommit = commit;
    }
    
    /**
     * Test method for {@link de.fau.osr.core.vcs.interfaces.VcsClient#getBranchList()}.
     */
    @Test
    public void testGetBranchList() {
        Iterator<String> branchList = null;
         branchList = client.getBranchList();

        assertNotNull(branchList);
        assertTrue(branchList.hasNext());
    }

    /**
     * Test method for {@link de.fau.osr.core.vcs.interfaces.VcsClient#getCommitList()}.
     */
    @Test
    public void testGetCommitList() {
        Iterator<String> commitList = null;
        commitList = client.getCommitList();
        assertNotNull(commitList);
        assertTrue(commitList.hasNext());
    }

    /**
     * Test method for {@link de.fau.osr.core.vcs.interfaces.VcsClient#getCommitFiles(java.lang.String)}.
     */
    @Test
    public void testGetCommitFiles() {
        Iterator<CommitFile> commitFileList = null;
        Iterator<String> commitList = null;
        commitList = client.getCommitList();
        if(commitList.hasNext()){
            commitFileList = client.getCommitFiles(commitList.next()).get().iterator();
        }
        assertNotNull(commitFileList);
        assertTrue(commitFileList.hasNext());
    }

    /**
     * Test method for {@link de.fau.osr.core.vcs.interfaces.VcsClient#getCommitMessage(java.lang.String)}.
     */
    @Test
    public void getCommitMessageSimpleTest() {
        String commitMessage = null;
        Iterator<String> commitList = null;
        commitList = client.getCommitList();
        if(commitList.hasNext()){
            commitMessage = client.getCommitMessage(commitList.next());
        }

        assertNotNull(commitMessage);
    }

}
