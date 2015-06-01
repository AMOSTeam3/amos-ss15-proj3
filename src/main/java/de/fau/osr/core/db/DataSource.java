package de.fau.osr.core.db;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

/**
 * Abstract <tt>DataSource</tt> to use persistent information
 * Created by Dmitry Gorelenkov on 03.05.2015.
 */
public abstract class DataSource {
    protected SetMultimap<String, String> cachedReqCommitRelations;

    /**
     * perform computing, and gets set of all requirements to commit relations
     * @return SetMultimap of relations
     * @throws IOException
     */
    protected abstract SetMultimap<String, String> doGetAllReqCommitRelations() throws IOException;
    protected abstract void doAddReqCommitRelation(String reqId, String commitId) throws IOException, OperationNotSupportedException ;
    protected abstract void doRemoveReqCommitRelation(String reqId, String commitId) throws IOException, OperationNotSupportedException;


    /* default implementations */

    /**
     * clears the cached data
     */
    public void clearCache(){
        this.cachedReqCommitRelations = null;
    }

    /**
     * adds new relation to data source
     * @param reqId requirement part of the relation
     * @param commitId commit part of the relation
     * @throws IOException
     */
    public void addReqCommitRelation(String reqId, String commitId) throws IOException, OperationNotSupportedException {
        doAddReqCommitRelation(reqId, commitId);
        clearCache();
    }


    /**
     * removes relation from data source
     * @param reqId requirement part of the relation
     * @param commitId commit part of the relation
     * @throws IOException
     */
    public void removeReqCommitRelation(String reqId, String commitId) throws IOException, OperationNotSupportedException {
        doRemoveReqCommitRelation(reqId, commitId);
        clearCache();
    }

    /**
     * get commit ids by requirement id
     * @param reqId requirement to search for
     * @return commit ids related to {@code reqId}
     * @throws IOException
     */
    public Set<String> getCommitRelationByReq(String reqId) throws IOException {
        SetMultimap<String, String> relations = getAllReqCommitRelations();
        return relations.get(reqId);
    }

    /**
     * get requirement id by commit id
     * @param commitId commit id to search for
     * @return all requirements that are related to the {@code commitId}
     * @throws IOException
     */
    public Set<String> getReqRelationByCommit(String commitId) throws IOException {
        SetMultimap<String, String> relations = getAllReqCommitRelations();
        SetMultimap<String, String> relationsComReq = HashMultimap.create();
        Multimaps.invertFrom(relations, relationsComReq);
        return relationsComReq.get(commitId);
    }

    /**
     * check if relation already exists
     * @param reqId
     * @param commitId
     * @return true if relation already exists, false otherwise
     * @throws IOException
     */
    public boolean isReqCommitRelationExists(String reqId, String commitId) throws IOException{
        ArrayList<String> reqs = Lists.newArrayList(getReqRelationByCommit(commitId));
        return reqs.contains(reqId);
    }

    /**
     * if relation already exists, changes it, if not, adds it
     * @param oldReqId
     * @param oldCommit
     * @param newReqId
     * @param newCommit
     * @throws IOException
     */
    public void setReqCommitRelation(String oldReqId, String oldCommit, String newReqId, String newCommit) throws IOException, OperationNotSupportedException {
        if(isReqCommitRelationExists(oldReqId, oldCommit)){
            removeReqCommitRelation(oldReqId, oldCommit);
        }
        addReqCommitRelation(newReqId, newCommit);
    }


    public void addReqCommitRelations(String reqId, Iterable<String> commitIds) throws IOException, OperationNotSupportedException {
        for (String commitId : commitIds){
            addReqCommitRelation(reqId, commitId);
        }
    }

    public void addReqCommitRelations(Iterable<String> reqIds, String commitId) throws IOException, OperationNotSupportedException {
        for (String reqId : reqIds){
            addReqCommitRelation(reqId, commitId);
        }
    }

    /**
     * try to get cached version of set of all relations, creates cached version, if it was was not created before
     * @return SetMultimap of all requirement to commit relations
     * @throws IOException
     */
    public SetMultimap<String, String> getAllReqCommitRelations() throws IOException {
        if (!isCachedReqCommitRelations()){
            doCacheReqCommitRelations(doGetAllReqCommitRelations());
        }

        return doGetCachedReqCommitRelations();
    }

    /**
     * used for default implementation of getAllReqCommitRelations
     * @param setToCache set should be cached
     */
    protected void doCacheReqCommitRelations(SetMultimap<String, String> setToCache){
        cachedReqCommitRelations = setToCache;
    }

    /**
     * used for default implementation of getAllReqCommitRelations
     * @return cached set of data
     */
    protected SetMultimap<String, String> doGetCachedReqCommitRelations(){
        return cachedReqCommitRelations;
    }

    /**
     * used for default implementation of getAllReqCommitRelations
     * @return true if ReqCommitRelations set is cached
     */
    protected boolean isCachedReqCommitRelations(){
        return cachedReqCommitRelations != null;
    }

}
