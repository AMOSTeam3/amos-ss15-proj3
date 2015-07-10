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
package de.fau.osr.vcs.impl;


import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;

import javax.naming.OperationNotSupportedException;

import org.eclipse.jgit.lib.ObjectId;
import org.junit.Test;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.SetMultimap;

import de.fau.osr.PublicTestData;
import de.fau.osr.core.db.DataSource;
import de.fau.osr.core.vcs.AnnotatedLine;
import de.fau.osr.core.vcs.AnnotatedWords;
import de.fau.osr.core.vcs.impl.GitBlameOperation;
import de.fau.osr.core.vcs.impl.GitVcsClient;

public class GitBlameOperationTest {
    GitVcsClient git;
    
    public GitBlameOperationTest() throws Exception {
    	git = new GitVcsClient(getClass().getResource("/TestRepository2/git").getPath());
    }
    
    @SuppressWarnings("unchecked")
	@Test
    public void testSimpleWordBlame() throws Exception {
    	BiFunction<String,Integer,Iterator<Integer>> putBlame = (s,i) -> {
    		if(s.compareTo("44ede09bf7dc2180f0ae7d614ae9fbccf549deaa") == 0)
    			return Collections.singleton(1).iterator();
    		return null;
    	};
    	AnnotatedWords w = new GitBlameOperation(git, "TestFile3", putBlame).wordBlame();
    	ObjectId commit1 = ObjectId.fromString("bc87c2039d1e14d5fa0131d77780eaa3b2cc627c"),
    			 commit2 = ObjectId.fromString("44ede09bf7dc2180f0ae7d614ae9fbccf549deaa"),
    			 commit3 = ObjectId.fromString("d97ad6e826b473098b4396aa09c638487305c572");
    	assertArrayEquals(new List[] {
    		Lists.newArrayList(1,commit1),
    		Lists.newArrayList(1,commit2),
    		Lists.newArrayList(1,commit2),
    		Lists.newArrayList(1,commit2),
    		Lists.newArrayList(1,commit2),
    		Lists.newArrayList(1,commit2),
    		Collections.singletonList(commit3),
    		Collections.singletonList(commit3),
    	}, w.annotations);
    	DataSource testDataSource = new DataSource() {

			@Override
			protected SetMultimap<String, String> doGetAllReqCommitRelations()
					throws IOException {
				return ImmutableSetMultimap.of(
						"a", commit1.name(),
						"42", commit2.name(),
						"ßäµ", commit3.name()
				);
			}

			@Override
			protected void doAddReqCommitRelation(String reqId, String commitId)
					throws IOException, OperationNotSupportedException {
				fail("add relation may not be called for lookup");
			}

			@Override
			protected void doRemoveReqCommitRelation(String reqId,
					String commitId) throws IOException,
					OperationNotSupportedException {
				fail("remove relation may not be called for lookup");
			}
    		
    	};
    	List<AnnotatedLine> lines = AnnotatedLine.wordsToLine(w, testDataSource);
    	assertEquals(Lists.newArrayList(
    			new AnnotatedLine(Lists.newArrayList("a", "42"), "1 3"),
    			new AnnotatedLine(Lists.newArrayList("ßäµ", "42"), "Test 2 100"))
    	, lines);
    }
}
