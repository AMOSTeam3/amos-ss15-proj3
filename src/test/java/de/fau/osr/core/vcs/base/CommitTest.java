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
}