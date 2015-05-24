package de.fau.osr.core.vcs.base;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test class for Commit
 * Created by Rajab Kaoneka on 18.05.2015.
 */
public class CommitTest {

    private List<String> reqsList;
    private List<CommitFile> commitFileList;

    @Before
    public void setUp(){
        reqsList = new ArrayList<>();
        reqsList.add("req1");
        reqsList.add("req2");
        commitFileList= (new ArrayList<>());
        CommitFile commitFile=new CommitFile(new File("."),new File("."),CommitState.RENAMED,"commitID","DataChanged");
        commitFileList.add(commitFile);
    }



    @Test
    public void CommitConstructor_idField_Test(){
        Commit commitToTest = new Commit("new_id", null, null, null);
        assertEquals("commit id should be equal the initialization id", "new_id", commitToTest.id);
    }

    @Test
    public void CommitConstructor_CommitIdInitializedWithNoUsualChars_allTheCharsReturnedProperly(){
        String[] ids = {"   ", "$&/&$//", "09084§§§", " kl%45$$%$%", "\"\"__ __\"\"", "", null};
        for (String id : ids){
            Commit commitToTest = new Commit(id, null, null, null);
            assertEquals("commit id should be equal the initialization id", id, commitToTest.id);
        }
    }

    @Test
    public void CommitConstructor_messageField_Test(){
        Commit commitToTest = new Commit(null, "msg ok", null, null);
        assertEquals("message should be equal the initialization message", "msg ok", commitToTest.message);
    }

    @Test
    public void CommitConstructor_requirementsField_Test() {
        Commit commitToTest = new Commit(null, null, reqsList, null);
        assertEquals("requirements should be equal the initialization requirements", reqsList, commitToTest.requirements);
    }

    @Test
    public void CommitConstructor_requirementsField_Empty_Test() {
        List<String> req = new ArrayList<>();
        Commit commitToTest = new Commit(null, null, req, null);
        assertEquals("requirements should be equal the initialization requirements", req, commitToTest.requirements);
    }

    @Test
    public void CommitConstructor_filesField_Test(){
        Commit commitToTest = new Commit(null, null, null, commitFileList);
        assertEquals("CommitFiles should be equal the initialization CommitFiles", commitFileList, commitToTest.files);
    }

    @Test
    public void CommitConstructor_EverythingInitializedwithNull_EveryPropertyReturnsNull_Test(){
        Commit commitToTest = new Commit(null, null, null, null);
        assertEquals("Initialized with commitfiles = Null, files should return Null", null, commitToTest.files);
        assertEquals("Initialized with commitId = Null, commitId should return Null", null, commitToTest.id);
        assertEquals("Initialized with Messages = Null, Messages should return Null", null, commitToTest.message);
        assertEquals("Initialized with Requiremnts = Null, Requirements should return Null", null, commitToTest.requirements);
    }

    @Test
    public void CommitConstructor_EverythingInitializedwithObjects_PropertiesReturnsSameObjects_Test(){
        Commit commitToTest = new Commit("new_id", "Message",reqsList, commitFileList);
        assertEquals("CommitFiles should be equal the initialization CommitFiles", commitFileList, commitToTest.files);
        assertEquals("Initialized with commitId = \"new_id\", commitId should return \"new_id\"", "new_id", commitToTest.id);
        assertEquals("Initialized with Messages = \"Message\", Messages should return \"Message\"", "Message", commitToTest.message);
        assertEquals("Initialized with Requiremnts List, The property requirenets should return the same list", reqsList, commitToTest.requirements);
    }


}