package de.fau.osr.core.vcs.base;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Rajab Kaoneka on 18.05.2015.
 */
public class CommitTest {

    @Test
    public void CommitConstructor_idField_Test(){
        Commit commitToTest = new Commit("new_id", null, null, null);
        assertEquals("commit id should be equal the initialization id", "new_id", commitToTest.id);
    }

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


}