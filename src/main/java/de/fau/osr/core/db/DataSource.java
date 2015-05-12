package de.fau.osr.core.db;

import com.google.common.collect.Lists;
import com.google.common.collect.SetMultimap;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Dmitry Gorelenkov on 03.05.2015.
 */
public abstract class DataSource {
    /**
     * adds new relation to data source
     * @param reqId requirement part of the relation
     * @param commitId commit part of the relation
     * @throws Exception
     */
    public abstract void addReqCommitRelation(Integer reqId, String commitId) throws Exception;

    /**
     * removes relation from data source
     * @param reqId requirement part of the relation
     * @param commitId commit part of the relation
     * @throws Exception
     */
    public abstract void removeReqCommitRelation(Integer reqId, String commitId) throws Exception;

    /**
     * get commit ids by requirement id
     * @param reqId requirement to search for
     * @return commit ids related to {@code reqId}
     * @throws Exception
     */
    public abstract Iterable<String> getCommitRelationByReq(Integer reqId) throws Exception;

    /**
     * get requirement id by commit id
     * @param commitId commit id to search for
     * @return all requirements that are related to the {@code commitId}
     * @throws IOException
     */
    public abstract Iterable<Integer> getReqRelationByCommit(String commitId) throws Exception;



    /* default implementations */

    /**
     * check if relation already exists
     * @param reqId
     * @param commitId
     * @return true if relation already exists, false otherwise
     * @throws Exception
     */
    public boolean isReqCommitRelationExists(Integer reqId, String commitId) throws Exception{
        ArrayList<Integer> reqs = Lists.newArrayList(getReqRelationByCommit(commitId));
        return reqs.contains(reqId);
    }

    /**
     * if relation already exists, changes it, if not, adds it
     * @param oldReqId
     * @param oldCommit
     * @param newReqId
     * @param newCommit
     * @throws Exception
     */
    public void setReqCommitRelation(Integer oldReqId, String oldCommit, Integer newReqId, String newCommit) throws Exception {
        if(isReqCommitRelationExists(oldReqId, oldCommit)){
            removeReqCommitRelation(oldReqId, oldCommit);
        }
        addReqCommitRelation(newReqId, newCommit);
    }


    public void addReqCommitRelations(Integer reqId, Iterable<String> commitIds) throws Exception {
        for (String commitId : commitIds){
            addReqCommitRelation(reqId, commitId);
        }
    }

    public void addReqCommitRelations(Iterable<Integer> reqIds, String commitId) throws Exception {
        for (Integer reqId : reqIds){
            addReqCommitRelation(reqId, commitId);
        }
    }

    public abstract SetMultimap<String, String> getAllReqCommitRelations() throws IOException;
}
