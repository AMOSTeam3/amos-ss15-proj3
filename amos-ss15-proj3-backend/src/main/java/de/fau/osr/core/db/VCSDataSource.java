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
package de.fau.osr.core.db;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import de.fau.osr.core.vcs.interfaces.VcsClient;
import de.fau.osr.util.parser.CommitMessageParser;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <tt>DataSource</tt> object for VCS
 * Created by Dmitry Gorelenkov on 14.05.2015.
 */
public class VCSDataSource extends DataSource {

    private VcsClient vcsClient;
    private CommitMessageParser msgParser;

    /**
     * <tt>DataSource</tt> object for VCS
     * Can only query information.
     * @param vcs vcs client to use
     * @param parser parser for messages in vcs client commits
     */
    public VCSDataSource(VcsClient vcs, CommitMessageParser parser) {
        vcsClient = vcs;
        msgParser = parser;
    }

    @Override
    public void doAddReqCommitRelation(String reqId, String commitId) throws IOException, OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }

    @Override
    public void doRemoveReqCommitRelation(String reqId, String commitId) throws IOException, OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }

    @Override
    public HashMultimap<String, String> doGetAllReqCommitRelations() throws IOException {
        HashMultimap<String, String> allRelations = HashMultimap.create();
        ArrayList<String> commitIds = Lists.newArrayList(vcsClient.getCommitList());
        String message;
        List<String> reqs;
        for(String commit : commitIds){
            message = vcsClient.getCommitMessage(commit);
            reqs = msgParser.parse(message);
            for(String reqId : reqs){
                allRelations.put(reqId, commit);
            }
        }

        return allRelations;
    }
}
