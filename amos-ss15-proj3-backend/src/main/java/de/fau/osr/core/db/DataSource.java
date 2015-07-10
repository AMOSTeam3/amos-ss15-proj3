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
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import de.fau.osr.core.Requirement;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract <tt>DataSource</tt> to use persistent information
 * Created by Dmitry Gorelenkov on 03.05.2015.
 */
public abstract class DataSource {
    protected SetMultimap<String, String> cachedReqCommitRelations;
    protected Set<Requirement> cachedAllRequirements;

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
        this.cachedAllRequirements = null;
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
     * @return true if relation already exists, false otherwise
     * @throws IOException
     */
    public boolean isReqCommitRelationExists(String reqId, String commitId) throws IOException{
        ArrayList<String> reqs = Lists.newArrayList(getReqRelationByCommit(commitId));
        return reqs.contains(reqId);
    }

    /**
     * if relation already exists, changes it, if not, adds it
     * @param oldReqId old state requirement id
     * @param oldCommit old state commit id
     * @param newReqId new state requirement id
     * @param newCommit new state commit id
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
     * try to get cached version of all requirements, creates cached version, if it was was not created before
     * @return set of all requirements, as data objects
     * @throws IOException
     */
    public Set<Requirement> getAllRequirements() throws IOException {
        if (!isCachedAllReqs()) {
            doCacheAllReqs(doGetAllRequirements());
        }

        return doGetCachedAllRequirements();
    }

    /**
     * saves (creates) or updates requirement object in data source
     * clears cache and calls doSaveOrUpdateRequirement()
     * @param id id of requirement
     * @param title new title
     * @param description new description
     * @throws IOException
     * @throws OperationNotSupportedException
     */
    public void saveOrUpdateRequirement(String id, String title, String description) throws IOException, OperationNotSupportedException {
        doSaveOrUpdateRequirement(id, title, description);
        clearCache();
    }

    /**
     * save or update method to override by subclasses
     * @see DataSource#saveOrUpdateRequirement(String, String, String)
     */
    protected void doSaveOrUpdateRequirement(String id, String title, String description) throws IOException, OperationNotSupportedException {
        throw new OperationNotSupportedException("cant update from this data source");
    }

    /**
     * @return cached requirement or null
     */
    protected Set<Requirement> doGetCachedAllRequirements() {
        return this.cachedAllRequirements;
    }

    /**
     * put requirements to cache
     */
    protected void doCacheAllReqs(Set<Requirement> requirements) {
        this.cachedAllRequirements = requirements;
    }

    /**
     * check if requirements for getAllRequirements() are cached
     * @return true if requirements cached
     */
    protected boolean isCachedAllReqs() {
        return cachedAllRequirements != null;
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

    /**
     * default implementation for retrieving all requirements.
     * {@link DataSource#getAllReqCommitRelations()} is used
     * @return requirement data objects
     * @throws IOException
     */
    protected Set<Requirement> doGetAllRequirements() throws IOException {
        SetMultimap<String, String> allRelations = getAllReqCommitRelations();

        Set<String> reqIds = allRelations.keySet();
        Set<Requirement> result = new HashSet<>();

        for (String reqId : reqIds) {
            Set<String> commits = getCommitRelationByReq(reqId);
            result.add(new Requirement(reqId, null, null, commits, 0));
        }

        return result;
    }

}
