package de.fau.osr.core.db;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

/**
 * Created by Dmitry Gorelenkov on 05.05.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class DataSourceTest {
    @Mock
    DataSource dataSourceMock;

    @Before
    public void setUp() {
        dataSourceMock = Mockito.spy(DataSource.class);
    }

    @Test
    public void isReqCommitRelationExistsTest() throws Exception {

        //given some test data returned
        Mockito.doReturn(new HashSet<String>()).when(dataSourceMock).getReqRelationByCommit(anyString());
        Mockito.doReturn(new HashSet<>(Arrays.asList("5, 4, 3, 2, 1".split(", ")))).when(dataSourceMock).getReqRelationByCommit("commit");
        //then
        assertTrue(dataSourceMock.isReqCommitRelationExists("5", "commit"));
        assertFalse(dataSourceMock.isReqCommitRelationExists("5", "commit_"));
        assertFalse(dataSourceMock.isReqCommitRelationExists("6", "commit"));
    }

    @Test
    public void setReqCommitRelation_relAlreadyExists_removeOldThanAdd_Test() throws Exception {
        //given
        String reqId = "5";
        String commitId = "commit";
        String reqIdNew = "6";
        String commitIdNew = "commit_new";
        //stub relation exists
        Mockito.doReturn(true).when(dataSourceMock).isReqCommitRelationExists(reqId, commitId);
        //when
        dataSourceMock.setReqCommitRelation(reqId, commitId, reqIdNew, commitIdNew);
        //than "remove" and "add" called once
        Mockito.verify(dataSourceMock).removeReqCommitRelation(reqId, commitId);
        Mockito.verify(dataSourceMock).addReqCommitRelation(reqIdNew, commitIdNew);

    }

    @Test
    public void setReqCommitRelation_noRelationExists_doesNotCallRemoveRelation_Test() throws Exception {
        //given
        Mockito.doReturn(false).when(dataSourceMock).isReqCommitRelationExists("1", "commit_old");
        //when
        dataSourceMock.setReqCommitRelation("1", "commit_old", "2", "com_new");
        //then
        Mockito.verify(dataSourceMock, never()).removeReqCommitRelation(anyString(), anyString());
        Mockito.verify(dataSourceMock).addReqCommitRelation("2", "com_new");
    }

    @Test
    public void addReqCommitRelations_addManyByReq_addReqCommitRelationCalledSoMuchTimesWithTheSamereId_Test() throws Exception {
        //given
        List<String> coms = Arrays.asList("1,commit,test,something, non trimmed  , text".split(","));
        //when
        dataSourceMock.addReqCommitRelations("5", coms);
        //then
        Mockito.verify(dataSourceMock, times(coms.size())).addReqCommitRelation(eq("5"), anyString());
    }

    @Test
    public void addReqCommitRelations_addManyByCommit_addReqCommitRelationCalledSoMuchTimesWithTheSameCommitId_Test() throws Exception {
        //given
        List<String> reqs = Arrays.asList("1, 2, 3, 4, 5000".split(", "));
        //when
        dataSourceMock.addReqCommitRelations(reqs, "someCommit");
        //then
        Mockito.verify(dataSourceMock, times(reqs.size())).addReqCommitRelation(anyString(), eq("someCommit"));
    }

    @Test
    public void getCommitRelationByReqTest() throws Exception {
        //given
        SetMultimap<String, String> allReqComRelations = HashMultimap.create();
        allReqComRelations.put("req1","commit2");
        allReqComRelations.put("req1","commit3");
        allReqComRelations.put("req4","commit4");
        allReqComRelations.put("req5","commit2");
        allReqComRelations.put("req6","commit3");
        allReqComRelations.put("req77","commit77");
        Mockito.doReturn(allReqComRelations).when(dataSourceMock).getAllReqCommitRelations();
        //when
        Iterable<String> commsFor1 = dataSourceMock.getCommitRelationByReq("req1");
        Iterable<String> commsFor5 = dataSourceMock.getCommitRelationByReq("req5");
        Iterable<String> commsFor77 = dataSourceMock.getCommitRelationByReq("req77");
        //then
        Set<String> expFor1 = new HashSet<>();
        expFor1.add("commit2");
        expFor1.add("commit3");
        assertEquals(expFor1, commsFor1);

        Set<String> expFor5 = new HashSet<>();
        expFor5.add("commit2");
        assertEquals(expFor5, commsFor5);

        Set<String> expFor77 = new HashSet<>();
        expFor77.add("commit77");
        assertEquals(expFor77, commsFor77);
    }

    @Test
    public void getReqRelationByCommitTest() throws Exception {
        //given
        SetMultimap<String, String> allReqComRelations = HashMultimap.create();
        allReqComRelations.put("req1","commit2");
        allReqComRelations.put("req1","commit3");
        allReqComRelations.put("req4","commit4");
        allReqComRelations.put("req5","commit2");
        allReqComRelations.put("req6","commit3");
        allReqComRelations.put("req77","commit77");
        Mockito.doReturn(allReqComRelations).when(dataSourceMock).getAllReqCommitRelations();
        //when
        Iterable<String> reqsFor2 = dataSourceMock.getReqRelationByCommit("commit2");
        Iterable<String> reqsFor3 = dataSourceMock.getReqRelationByCommit("commit3");
        Iterable<String> reqsFor77 = dataSourceMock.getReqRelationByCommit("commit77");
        //then
        Set<String> expFor2 = new HashSet<>();
        expFor2.add("req1");
        expFor2.add("req5");
        assertEquals(expFor2, reqsFor2);

        Set<String> expFor3 = new HashSet<>();
        expFor3.add("req1");
        expFor3.add("req6");
        assertEquals(expFor3, reqsFor3);

        Set<String> expFor77 = new HashSet<>();
        expFor77.add("req77");
        assertEquals(expFor77, reqsFor77);
    }
}