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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;

/**
 * Created by Dmitry Gorelenkov on 14.05.2015.
 */
public class CompositeDataSourceTest {

    DataSource[] dataSourceMocks;
    DataSource mainSource;
    int numOfOtherDataSources = 5;
    CompositeDataSource compDsUnderTest;

    @Before
    public void setUp() {
        mainSource = Mockito.mock(DataSource.class, new EmptySetMultimapAnswer());

        DataSource[] dataSourceMocks = new DataSource[numOfOtherDataSources];
        for (int i = 0; i < numOfOtherDataSources; i++) {
            dataSourceMocks[i] =  Mockito.mock(DataSource.class, new EmptySetMultimapAnswer());
        }

        compDsUnderTest = new CompositeDataSource(mainSource, dataSourceMocks);

    }

    @Test
    public void addSourceTest() throws Exception {
        DataSource oneSourceMore = Mockito.mock(DataSource.class, new EmptySetMultimapAnswer());
        //given
        compDsUnderTest.addSource(oneSourceMore);
        //when
        compDsUnderTest.getAllReqCommitRelations();
        //then
        Mockito.verify(oneSourceMore).getAllReqCommitRelations();

    }

    @Test
    public void addSourcesTest() throws Exception {
        DataSource oneSourceMore = Mockito.mock(DataSource.class, new EmptySetMultimapAnswer());
        DataSource oneSourceMore2 = Mockito.mock(DataSource.class, new EmptySetMultimapAnswer());
        //given
        compDsUnderTest.addSources(oneSourceMore, oneSourceMore2);
        //when
        compDsUnderTest.getAllReqCommitRelations();
        //then
        Mockito.verify(oneSourceMore).getAllReqCommitRelations();
        Mockito.verify(oneSourceMore2).getAllReqCommitRelations();
    }

    @Test
    public void addReqCommitRelationTest() throws Exception {
        //when
        compDsUnderTest.addReqCommitRelation("one", "two");
        //then
        Mockito.verify(mainSource).addReqCommitRelation("one", "two");
    }

    @Test
    public void removeReqCommitRelationTest() throws Exception {
        //when
        compDsUnderTest.removeReqCommitRelation("one", "two");
        //then
        Mockito.verify(mainSource).removeReqCommitRelation("one", "two");
    }

    @Test
    public void getAllReqCommitRelationsTest() throws Exception {
        //given
        DataSource one = Mockito.mock(DataSource.class);
        DataSource two = Mockito.mock(DataSource.class);
        HashMultimap<String, String> retOne = HashMultimap.create();
        HashMultimap<String, String> retTwo = HashMultimap.create();
        retOne.put("req1", "commit2");
        retOne.put("req1", "commit3");
        retOne.put("req4", "commit4");
        retOne.put("req77", "commit77");

        retTwo.put("req1", "commit2");
        retTwo.put("req1", "commit4");
        retTwo.put("req2", "commit3");

        Mockito.doReturn(retOne).when(one).getAllReqCommitRelations();
        Mockito.doReturn(retTwo).when(two).getAllReqCommitRelations();

        //when
        compDsUnderTest.addSources(one, two);
        SetMultimap<String, String> result = compDsUnderTest.getAllReqCommitRelations();

        //then
        SetMultimap<String, String> expected = HashMultimap.create();
        expected.putAll(retOne);
        expected.putAll(retTwo);
        assertEquals(expected, result);
    }
}

/**
 * helper class, need to define return empty SetMultimap for DataSource mocks
 */
class EmptySetMultimapAnswer implements Answer{

    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        return HashMultimap.create();
    }
}