package de.fau.osr.vcs.impl;

import com.google.common.collect.Lists;
import de.fau.osr.PublicTestData;
import de.fau.osr.core.db.DataSource;
import de.fau.osr.core.db.VCSDataSource;
import de.fau.osr.core.vcs.impl.GitVcsClient;
import de.fau.osr.core.vcs.interfaces.VcsClient.AnnotatedLine;
import de.fau.osr.util.AppProperties;
import de.fau.osr.util.parser.CommitMessageParser;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

public class GitBlameTest {
    GitVcsClient client;

    public GitBlameTest() throws IOException {
        client =  new GitVcsClient(PublicTestData.getGitTestRepo());
    }

    @Test
    public void getBlame() throws Exception {
        CommitMessageParser cmparser =  new CommitMessageParser(Pattern.compile(AppProperties.GetValue("RequirementPattern")));
        DataSource ds =  new VCSDataSource(client,cmparser);

        List<AnnotatedLine> blame = client.blame("TestFile4", ds);
        assertEquals(client.new AnnotatedLine(Lists.newArrayList("1"), "File 4"), blame.get(0));
        blame = client.blame("LICENSE", ds);
        for(AnnotatedLine line : blame) {
            assertEquals(Collections.emptyList(), line.getRequirements());
        }
    }
}
