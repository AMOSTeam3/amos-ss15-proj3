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
