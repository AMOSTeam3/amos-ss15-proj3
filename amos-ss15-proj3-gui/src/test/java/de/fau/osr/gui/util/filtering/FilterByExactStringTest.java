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
package de.fau.osr.gui.util.filtering;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import de.fau.osr.gui.Model.DataElements.Requirement;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

/**
 * @author Taleh Didover
 */
public class FilterByExactStringTest {

    @Test
    public void test_SimpleFiltering() throws Exception {
        Collection<Requirement> testdatas = new ArrayList<>();
        for (String word : "Java ist auch eine Insel".split(" ")) {
            testdatas.add(new Requirement(word));
        }
        Predicate<Requirement> filtering = new FilterByExactString("Java");

        Collection<Requirement> expected = Arrays.asList(new Requirement("Java"));
        Collection<Requirement> got = Collections2.filter(testdatas, filtering);
        assertTrue(Iterables.elementsEqual(expected, got));
    }

    @Test
    public void test_NoFiltering() throws Exception {
        Collection<Requirement> testdatas = new ArrayList<>();

        for (String word : "Java ist auch eine Insel".split(" ")) {
            testdatas.add(new Requirement(word));
        }
        Predicate<Requirement> filtering = new FilterByExactString("");


        Collection<Requirement> expected = new ArrayList<>(testdatas);
        Collection<Requirement> got = Collections2.filter(testdatas, filtering);
        assertTrue(Iterables.elementsEqual(expected, got));
    }
}