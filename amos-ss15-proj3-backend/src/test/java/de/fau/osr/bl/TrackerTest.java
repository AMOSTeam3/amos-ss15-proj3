/*
 * This file is part of Req-Tracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * Req-Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Req-Tracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Req-Tracker.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fau.osr.bl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import de.fau.osr.core.db.DBTestHelper;
import de.fau.osr.core.db.DataSource;
import de.fau.osr.core.vcs.AnnotatedLine;
import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.CommitState;
import de.fau.osr.core.vcs.impl.GitVcsClient;
import de.fau.osr.core.vcs.interfaces.VcsClient;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;

/**
 * @author Gayathery
 *
 */
public class TrackerTest {

    static VcsClient client;
    static Tracker tracker;
    
    static Tracker mockedTracker;
    static GitVcsClient mockedClient;
    static DataSource mockedSource;


    @BeforeClass
    public static void prepare() throws IOException {
        tracker = new Tracker(client, null, null, DBTestHelper.createH2SessionFactory());
        
        mockedClient = mock(GitVcsClient.class);
        mockedSource = mock(DataSource.class);
        mockedTracker = Mockito.spy(new Tracker(mockedClient, mockedSource, null, DBTestHelper.createH2SessionFactory()));
        
    }

    /**
     * Test method for {@link de.fau.osr.bl.Tracker#getCommitFilesForRequirementID(java.lang.String)}.
     */
    @Test
    public void testGetCommitFilesForRequirementID() throws IOException, GitAPIException {
      
        List<CommitFile> commitFilesList = new ArrayList<>();
        
        SetMultimap<String, String> commits = HashMultimap.create();
        commits.put("1","commit1");
        
        Mockito.doReturn(commits).when(mockedTracker).getAllCommitReqRelations();
        
        Mockito.doReturn(getSampleCommitFiles()).when(mockedClient).getCommitFiles("commit1");
        for(int i=2; i<=4; ++i)
        	Mockito.doReturn(emptyStream()).when(mockedClient).getCommitFiles("commit" + i);
        //no need blame here
        Mockito.doReturn(new ArrayList<AnnotatedLine>()).when(mockedTracker).getBlame(anyString());

        
        commitFilesList = mockedTracker.getCommitFilesForRequirementID("1");
        assertEquals(commitFilesList.size(), 2);
       

    }


    @Test
    public void testGetTotalReqLinkageTest() throws Exception {
       
        //stub for dataSource.getAllReqCommitRelations()
        SetMultimap<String, String> dbReqs = HashMultimap.create();
        dbReqs.put("1","commit1");
        dbReqs.put("1","commit2");
        dbReqs.put("2","commit3");
        dbReqs.put("3", "commit3");
        dbReqs.put("4", "commit4");

        Mockito.doReturn(dbReqs).when(mockedSource).getAllReqCommitRelations();

        //when
        SetMultimap<String, String> reqCommitLinkage = mockedTracker.getAllReqCommitRelations();

        //than
        SetMultimap<String, String> expected = HashMultimap.create();
        expected.putAll(dbReqs);

        for (Map.Entry<String, String> entry : expected.entries()){
            assertTrue(reqCommitLinkage.containsEntry(entry.getKey(), entry.getValue()));
        }
        assertEquals(reqCommitLinkage.size(), expected.size());
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testGetAllFilesAsString() throws IOException{
        
        String commitId = "a0b52e890fa0d7fad6563cf91ea6dcca52e9226f";
        List<String> allCommits =  new ArrayList<String>();
        allCommits.add(commitId);
        
        Mockito.doReturn(allCommits.iterator() ).when(mockedClient).getCommitList();
        
        Mockito.doReturn(getSampleCommitFiles()).when(mockedClient).getCommitFiles(commitId);
        
        Collection<String> files = mockedTracker.getAllFilesAsString();
        assertEquals(files.size(), 1);
        assertTrue(files.contains(getSampleCommitFiles().get().collect(Collectors.toList()).
        		get(0).newPath.getPath()));
    }
    
    private Supplier<Stream<CommitFile>> getSampleCommitFiles(){
        String commitId = "a0b52e890fa0d7fad6563cf91ea6dcca52e9226f";
        List<CommitFile> commitFile = new ArrayList<CommitFile>();
        File testFile = new File("amos-ss15-proj3-gui\\src\\main\\java\\de\\fau\\osr\\gui\\Controller\\GuiController.java");
        commitFile.add(new CommitFile(null, new File("InitClass.java"),testFile, CommitState.DELETED, commitId, null));
        commitFile.add(new CommitFile(null, null,testFile, CommitState.ADDED, commitId, null));
        return commitFile::stream;
    }
    
    private static <T> Supplier<Stream<T>> emptyStream() {
    	return Stream::empty;
    }

}
