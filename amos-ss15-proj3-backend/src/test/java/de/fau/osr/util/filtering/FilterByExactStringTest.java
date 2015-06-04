package de.fau.osr.util.filtering;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Taleh Didover
 */
public class FilterByExactStringTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception {

    }

    public void test_SimpleFiltering() throws Exception {
        Collection<String> testdatas = Arrays.asList("Java ist auch eine Insel".split(" "));
        Predicate<String> filtering = new FilterByExactString("Java");

        Collection<String> expected = Arrays.asList("Java");
        Collection<String> got = Collections2.filter(testdatas, filtering);
        assertTrue(Iterables.elementsEqual(expected, got));
    }

    public void test_NoFiltering() throws Exception {
        Collection<String> testdatas = Arrays.asList("Java ist auch eine Insel".split(" "));
        Predicate<String> filtering = new FilterByExactString("");


        Collection<String> expected = new ArrayList<>(testdatas);
        Collection<String> got = Collections2.filter(testdatas, filtering);
        assertTrue(Iterables.elementsEqual(expected, got));
    }
}