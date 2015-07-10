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
/**
 * 
 */
package de.fau.osr.app;

import de.fau.osr.PublicTestData;
import de.fau.osr.core.vcs.base.Commit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Florian Gerdes
 */
public class CommitRequirementsListAppTest {
    /**
     * outContent: The Outputstream for our tested Class CommitFileListingApp is redirected to this ByteArrayOutputStream; so that we can compare.
     * testData: To get direct access to the commits in the test data repository. Please note, that the commits are created from the related CSVFile
     * not from the repository itself.
     */
    private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static PublicTestData testData = new PublicTestData();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void mainTest(){
        CommitRequirementsListApp.main(new String [] {"-repo", PublicTestData.getGitTestRepo()});
        String expected = buildOutputString(testData.getCommitsWithReqIds());
        assertEquals(outContent.toString(), expected);
    }

    private String buildOutputString(List<Commit> commits){
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(result);
        for(Commit commit: commits){
            for(String i: commit.getRequirements()){
                stream.println("commit " + commit.getId() + " references Req-" + i);
            }
        }
        return result.toString();
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
    }
}
