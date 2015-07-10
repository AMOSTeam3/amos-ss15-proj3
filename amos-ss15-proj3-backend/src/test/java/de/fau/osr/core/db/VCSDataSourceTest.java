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
import de.fau.osr.PublicTestData;
import de.fau.osr.core.vcs.base.VcsEnvironment;
import de.fau.osr.core.vcs.interfaces.VcsClient;
import de.fau.osr.util.AppProperties;
import de.fau.osr.util.parser.CommitMessageParser;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

/**
 * Created by Dmitry Gorelenkov on 14.05.2015.
 */
public class VCSDataSourceTest {
    static VcsClient client;
    static VCSDataSource vcsDs;

    @BeforeClass
    public static void prepare() throws IOException {
        client =  VcsClient.connect(VcsEnvironment.GIT, PublicTestData.getGitTestRepo());
        vcsDs = new VCSDataSource(client, new CommitMessageParser(Pattern.compile(AppProperties.GetValue("RequirementPattern"))));
    }

    @Test(expected = OperationNotSupportedException.class)
    public void addReqCommitRelationTest() throws Exception {
        vcsDs.addReqCommitRelation("1","2");
    }

    @Test(expected = OperationNotSupportedException.class)
    public void removeReqCommitRelationTest() throws Exception {
        vcsDs.removeReqCommitRelation("1","2");
    }

    @Test
    public void getAllReqCommitRelationsTest() throws Exception {
            SetMultimap<String, String> result = vcsDs.getAllReqCommitRelations();
            SetMultimap<String, String> expected = HashMultimap.create();
            expected.put("0","f3196114a214a91ae3994b6cf6424d8347b2e918");
            expected.put("1","b0b5d16e8071c775bdcd1b2d0b1cca464917780b");
            expected.put("6","f3196114a214a91ae3994b6cf6424d8347b2e918");
            expected.put("11","dee896c8d52af6bc0b00982ad2fcfca2d9d003dc");
            assertEquals(result, expected);
    }
}