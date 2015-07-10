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
package de.fau.osr.core.vcs.base;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Test class for Commit
 * Created by Rajab Kaoneka on 18.05.2015.
 */
public class CommitTest {

    private Set<String> reqsList;
    private List<CommitFile> commitFileList;

    @Before
    public void setUp(){
        reqsList = new HashSet<>();
        reqsList.add("req1");
        reqsList.add("req2");
        commitFileList= (new ArrayList<>());
        CommitFile commitFile=new CommitFile(new File("/"), new File("."),new File("."),CommitState.RENAMED,"commitID","DataChanged");
        commitFileList.add(commitFile);
    }



    @Test
    public void CommitConstructor_idField_Test(){
        Commit commitToTest = new Commit("new_id", null, null, null);
        assertEquals("commit id should be equal the initialization id", "new_id", commitToTest.getId());
    }

    @Test
    public void CommitConstructor_CommitIdInitializedWithNoUsualChars_allTheCharsReturnedProperly(){
        String[] ids = {"   ", "$&/&$//", "09084§§§", " kl%45$$%$%", "\"\"__ __\"\"", "", null};
        for (String id : ids){
            Commit commitToTest = new Commit(id, null, null, null);
            assertEquals("commit id should be equal the initialization id", id, commitToTest.getId());
        }
    }

    @Test
    public void CommitConstructor_messageField_Test(){
        Commit commitToTest = new Commit(null, "msg ok", null, null);
        assertEquals("message should be equal the initialization message", "msg ok", commitToTest.getMessage());
    }

    @Test
    public void CommitConstructor_requirementsField_Test() {
        Commit commitToTest = new Commit(null, null, reqsList, null);
        assertEquals("requirements should be equal the initialization requirements", reqsList, commitToTest.getRequirements());
    }

    @Test
    public void CommitConstructor_requirementsField_Empty_Test() {
        Set<String> req = new HashSet<>();
        Commit commitToTest = new Commit(null, null, req, null);
        assertEquals("requirements should be equal the initialization requirements", req, commitToTest.getRequirements());
    }

    @Test
    public void CommitConstructor_EverythingInitializedwithNull_EveryPropertyReturnsNull_Test(){
        Commit commitToTest = new Commit(null, null, null, null);
        assertEquals("Initialized with commitId = Null, commitId should return Null", null, commitToTest.getId());
        assertEquals("Initialized with Messages = Null, Messages should return Null", null, commitToTest.getMessage());
        assertEquals("Initialized with Requiremnts = Null, Requirements should return Null", null, commitToTest.getRequirements());
    }

    @Test
    public void CommitConstructor_EverythingInitializedwithObjects_PropertiesReturnsSameObjects_Test(){
        Commit commitToTest = new Commit("new_id", "Message",reqsList, null);
        assertEquals("Initialized with commitId = \"new_id\", commitId should return \"new_id\"", "new_id", commitToTest.getId());
        assertEquals("Initialized with Messages = \"Message\", Messages should return \"Message\"", "Message", commitToTest.getMessage());
        assertEquals("Initialized with Requiremnts List, The property requirenets should return the same list", reqsList, commitToTest.getRequirements());
    }


}