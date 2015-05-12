package de.fau.osr.core.db;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
        Mockito.when(dataSourceMock.getReqRelationByCommit(anyString())).thenReturn(new HashSet<String>());
        Mockito.when(dataSourceMock.getReqRelationByCommit("commit")).thenReturn(new HashSet<>(Arrays.asList("5, 4, 3, 2, 1".split(", "))));
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
}