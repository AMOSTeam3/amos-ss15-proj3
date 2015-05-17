package de.fau.osr.vcs.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import de.fau.osr.util.AppProperties;
import org.junit.Test;

import com.google.common.collect.Lists;

import de.fau.osr.PublicTestData;
import de.fau.osr.core.vcs.interfaces.VcsClient.AnnotatedLine;
import de.fau.osr.core.vcs.impl.GitVcsClient;
import de.fau.osr.util.parser.CommitMessageParser;

public class GitBlameTest {
	GitVcsClient client;
	
	public GitBlameTest() throws IOException {
		client =  new GitVcsClient(PublicTestData.getGitTestRepo());
	}
	
	@Test
	public void getBlame() throws Exception {
		CommitMessageParser cmparser =  new CommitMessageParser(Pattern.compile(AppProperties.GetValue("RequirementPattern")));
		List<AnnotatedLine> blame = client.blame("TestFile4", cmparser);
		assertEquals(Collections.singletonList(new AnnotatedLine(Lists.<String>newArrayList("1"), "File 4")), Lists.newArrayList(blame));
		blame = client.blame("LICENSE", cmparser);
		for(AnnotatedLine line : blame) {
			assertEquals(Collections.emptyList(), line.getRequirements());
		}
	}
}
