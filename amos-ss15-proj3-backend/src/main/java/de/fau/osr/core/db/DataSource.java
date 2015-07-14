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

    /**
     * perform computing, and gets set of all requirements to commit relations
     * @return SetMultimap of relations
     * @throws IOException
     */
    public abstract SetMultimap<String, String> getAllReqCommitRelations() throws IOException;
    /**
     * adds new relation to data source
     * @param reqId requirement part of the relation
     * @param commitId commit part of the relation
     * @throws IOException
     */
    public abstract void addReqCommitRelation(String reqId, String commitId) throws IOException, OperationNotSupportedException ;
    /**
     * removes relation from data source
     * @param reqId requirement part of the relation
     * @param commitId commit part of the relation
     * @throws IOException
     */
    public abstract void removeReqCommitRelation(String reqId, String commitId) throws IOException, OperationNotSupportedException;

    /* default implementations */

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
     * default implementation for retrieving all requirements.
     * {@link DataSource#getAllReqCommitRelations()} is used
     * @return requirement data objects
     * @throws IOException
     */
    public Set<Requirement> getAllRequirements() throws IOException {
        SetMultimap<String, String> allRelations = getAllReqCommitRelations();

        Set<String> reqIds = allRelations.keySet();
        Set<Requirement> result = new HashSet<>();

        for (String reqId : reqIds) {
            Set<String> commits = getCommitRelationByReq(reqId);
            result.add(new Requirement(reqId, null, null, commits, 0));
        }

        return result;
    }

    /**
     * saves (creates) or updates requirement object in data source
     * @param id id of requirement
     * @param title new title
     * @param description new description
     * @throws IOException
     * @throws OperationNotSupportedException
     */
    public void saveOrUpdateRequirement(String id, String title, String description) throws IOException, OperationNotSupportedException {
        throw new OperationNotSupportedException("cant update from this data source");
    }

    /**
     * default implementation for getting all req - commit filters.
     * should be overridden by subclasses, and return SetMultimap of
     * requirement -> commit filter pairs
     * @return empty SetMultimap
     */
    public SetMultimap<String, String> getReqCommitFilters() {
        return HashMultimap.create();
    }

    /**
     * adds filter
     * default implementation do nothing
     * @param reqId requirement id
     * @param commitId commit id
     */
    public void addFilter(String reqId, String commitId) throws OperationNotSupportedException {
        throw new OperationNotSupportedException("cant add any filter from this data source");
    }

    /**
     * removes filter
     * default implementation do nothing
     * @param reqId requirement id
     * @param commitId commit id
     */
    public void removeFilter(String reqId, String commitId) throws OperationNotSupportedException {
        throw new OperationNotSupportedException("cant remove any filter from this data source");
    }

}
