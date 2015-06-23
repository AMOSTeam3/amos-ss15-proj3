package de.fau.osr.vcs.impl;


import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;

import org.eclipse.jgit.lib.ObjectId;
import org.junit.Test;

import com.beust.jcommander.internal.Lists;

import de.fau.osr.PublicTestData;
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
    }
}
