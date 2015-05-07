package de.fau.osr.vcs.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;

import org.junit.Test;

import com.google.common.collect.Lists;

import de.fau.osr.PublicTestData;
import de.fau.osr.bl.Tracker;
import de.fau.osr.core.db.CSVFileDataSource;
import de.fau.osr.core.db.DataSource;
import de.fau.osr.core.vcs.impl.GitVcsClient;
import de.fau.osr.core.vcs.interfaces.VcsClient.AnnotatedLine;
import de.fau.osr.util.parser.CommitMessageParser;

public class GitBlameTest {
	GitVcsClient client;
	
	public GitBlameTest() throws IOException {
		client =  new GitVcsClient(PublicTestData.getGitTestRepo());
	}
	
	@Test
	public void getBlame() throws Exception {
		Path repoFilePath = Paths.get(new File(PublicTestData.getGitTestRepo()).getAbsolutePath());
		Path dataSrcFilePath= repoFilePath.getParent().resolve("dataSource.csv");
		DataSource dataSource = new CSVFileDataSource(dataSrcFilePath.toFile());
		Tracker tracker = new Tracker(client);
		DataSource dataSrc = new CSVFileDataSource(dataSrcFilePath.toFile());
		Collection<AnnotatedLine> blame = client.blame("TestFile4", new CommitMessageParser());
		assertEquals(Collections.singletonList(new AnnotatedLine(Lists.<String>newArrayList("1"), "File 4")), Lists.newArrayList(blame));
		blame = client.blame("LICENSE", new CommitMessageParser());
		for(AnnotatedLine line : blame) {
			assertEquals(Collections.emptyList(), line.getRequirements());
		}
	}
}
