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
 * Created by Dmitry Gorelenkov on 14.05.2015.
 */
public class VCSDataSource extends DataSource {

    private VcsClient vcsClient;
    private CommitMessageParser msgParser;

    public VCSDataSource(VcsClient vcs, CommitMessageParser parser) {
        vcsClient = vcs;
        msgParser = parser;
    }

    @Override
    public void addReqCommitRelation(String reqId, String commitId) throws IOException, OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }

    @Override
    public void removeReqCommitRelation(String reqId, String commitId) throws IOException, OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }

    @Override
    public HashMultimap<String, String> getAllReqCommitRelations() throws IOException {
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
