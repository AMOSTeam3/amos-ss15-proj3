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
package de.fau.osr.core.db;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.io.Files;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

/**
 * Created by Dmitry Gorelenkov on 03.05.2015.
 * refactored from Taleh's tests
 * no good tests here, because temporary solution
 * anyway todo: mini framework to generate file content + big content tests
 */
public class CSVFileDataSourceTest {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private CSVFileDataSource csvFileDataSource;
    private File sourceFile;
    private char sep = CSVFileDataSource.SEPARATOR;
    private char qt = CSVFileDataSource.QUOTE_CHAR;
    private String lend = CSVFileDataSource.LINE_END;
    private Charset cs = CSVFileDataSource.CHARSET;

    @Before
    public void setUp() throws Exception {
        sourceFile = new File(tempFolder.getRoot(), "test_req_commit_dependencies.csv");
        csvFileDataSource = new CSVFileDataSource(sourceFile);

    }

    @After
    public void tearDown() throws Exception {
        sourceFile.delete();
    }


    @Test
    public void addReqCommitRelationTest() throws Exception {
        csvFileDataSource.addReqCommitRelation("1", "dee896c8d52af6bc0b00982ad2fcfca2d9d003dc");
        csvFileDataSource.addReqCommitRelation("1", "a8dc4129802939d620ce0bd3484a1f0538338a0e");
        csvFileDataSource.addReqCommitRelation("2", "dee896c8d52af6bc0b00982ad2fcfca2d9d003dc");

        String expectedFileContent = qt + "1" + qt + sep + qt + "dee896c8d52af6bc0b00982ad2fcfca2d9d003dc" + qt + lend;
        expectedFileContent += qt + "1" + qt + sep + qt + "a8dc4129802939d620ce0bd3484a1f0538338a0e" + qt + lend;
        expectedFileContent += qt + "2" + qt + sep + qt + "dee896c8d52af6bc0b00982ad2fcfca2d9d003dc" + qt + lend;
        String fileContent = Files.toString(sourceFile, cs);

        assertEquals(expectedFileContent, fileContent);

    }

    @Test
    public void removeReqCommitRelationTest() throws Exception {

    }

    @Test
    public void getCommitRelationByReqTest() throws Exception {

        String fileContent = qt + "5" + qt + sep + qt + "dee896c8d52af6bc0b00982ad2fcfca2d9d003dc" + qt + lend
                + qt + "4" + qt + sep + qt + "a8dc4129802939d620ce0bd3484a1f0538338a0e" + qt + lend
                + qt + "3" + qt + sep + qt + "a8dc4129802939d620ce0bd3484a1f0538338a0e" + qt + lend;

        Files.append(fileContent, sourceFile, cs);


        Iterable<String> acutalCommitIDs = csvFileDataSource.getCommitRelationByReq("5");
        Collection<String> expectedCommitIDs = new HashSet<>();
        expectedCommitIDs.add("dee896c8d52af6bc0b00982ad2fcfca2d9d003dc");

        assertEquals(acutalCommitIDs, expectedCommitIDs);
    }

    @Test
    public void getReqRelationByCommitTest() throws Exception {
        String fileContent = qt + "4" + qt + sep + qt + "dee896c8d52af6bc0b00982ad2fcfca2d9d003dc" + qt + lend
                + qt + "4" + qt + sep + qt + "a8dc4129802939d620ce0bd3484a1f0538338a0e" + qt + lend
                + qt + "5" + qt + sep + qt + "dee896c8d52af6bc0b00982ad2fcfca2d9d003dc" + qt + lend
                + qt + "3" + qt + sep + qt + "a8dc4129802939d620ce0bd3484a1f0538338a0e" + qt + lend;

        Files.append(fileContent, sourceFile, cs);


        Collection<String> acutalReqIDs = csvFileDataSource.getReqRelationByCommit("a8dc4129802939d620ce0bd3484a1f0538338a0e");
        Collection<String> expectedCommitIDs = new HashSet<>();
        expectedCommitIDs.add("4");
        expectedCommitIDs.add("3");

        assertEquals(acutalReqIDs, expectedCommitIDs);
    }

    @Test
    public void addAndGetCSVTest() throws IOException, OperationNotSupportedException {
        csvFileDataSource.addReqCommitRelation("1", "commit 1");
        csvFileDataSource.addReqCommitRelation("1", "commit");
        csvFileDataSource.addReqCommitRelation("1", "commit 1");
        csvFileDataSource.addReqCommitRelation("2", "commit 9000");
        csvFileDataSource.addReqCommitRelation("3", "a8dc4129802939d620ce0bd3484a1f0538338a0e");
        csvFileDataSource.addReqCommitRelation("4", "commit 1");
        csvFileDataSource.addReqCommitRelation("4", "commit 1");
        csvFileDataSource.addReqCommitRelation("1", "commit 1");

        HashSet<String> expectedCommitIDs = new HashSet<>();
        expectedCommitIDs.add("commit 1");
        expectedCommitIDs.add("commit");

        assertEquals(csvFileDataSource.getCommitRelationByReq("1"), expectedCommitIDs);


        Collection<String> expectedReqIDs = new HashSet<>();
        expectedReqIDs.add("1");
        expectedReqIDs.add("4");
        assertEquals(csvFileDataSource.getReqRelationByCommit("commit 1"), expectedReqIDs);

    }

    @Test
    public void getAllReqCommitRelationsTest() throws Exception {
        String fileContent = qt + "4" + qt + sep + qt + "dee896c8d52af6bc0b00982ad2fcfca2d9d003dc" + qt + lend
                + qt + "4" + qt + sep + qt + "a8dc4129802939d620ce0bd3484a1f0538338a0e" + qt + lend
                + qt + "5" + qt + sep + qt + "dee896c8d52af6bc0b00982ad2fcfca2d9d003dc" + qt + lend
                + qt + "3" + qt + sep + qt + "a8dc4129802939d620ce0bd3484a1f0538338a0e" + qt + lend;

        HashMultimap<String, String> relationsInFile = HashMultimap.create();
        relationsInFile.put("4", "dee896c8d52af6bc0b00982ad2fcfca2d9d003dc");
        relationsInFile.put("4", "a8dc4129802939d620ce0bd3484a1f0538338a0e");
        relationsInFile.put("5", "dee896c8d52af6bc0b00982ad2fcfca2d9d003dc");
        relationsInFile.put("3", "a8dc4129802939d620ce0bd3484a1f0538338a0e");

        Files.append(fileContent, sourceFile, cs);

        SetMultimap<String, String> relations = csvFileDataSource.getAllReqCommitRelations();

        assertEquals(relationsInFile, relations);
    }
}