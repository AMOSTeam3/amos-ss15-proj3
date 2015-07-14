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

import com.google.common.collect.SetMultimap;
import de.fau.osr.core.Requirement;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.util.Set;

/**
 * Caching proxy for data source
 * Created by Dmitry Gorelenkov on 14.07.2015.
 */
public class CachingDataSourceProxy extends DataSource {

    protected final DataSource target;

    public CachingDataSourceProxy(DataSource target) {
        this.target = target;
    }

    protected SetMultimap<String, String> cachedReqCommitRelations;
    protected SetMultimap<String, String> cachedReqCommitFilters;
    protected Set<Requirement> cachedAllRequirements;

    /**
     * clears the cached data
     */
    public void clearCache() {
        this.cachedReqCommitRelations = null;
        this.cachedAllRequirements = null;
        this.cachedReqCommitFilters = null;
    }

    /*-------------- Req-Commit relation operations --------------*/

    /**
     * try to get cached version of set of all relations, creates cached version, if it was was not created before
     *
     * @return SetMultimap of all requirement to commit relations
     * @throws IOException
     */
    public SetMultimap<String, String> getAllReqCommitRelations() throws IOException {
        if (!isCachedReqCommitRelations()) {
            cacheReqCommitRelations(target.getAllReqCommitRelations());
        }

        return getCachedReqCommitRelations();
    }

    @Override
    public void addReqCommitRelation(String reqId, String commitId) throws IOException, OperationNotSupportedException {
        target.addReqCommitRelation(reqId, commitId);
        clearCache();
    }

    @Override
    public void removeReqCommitRelation(String reqId, String commitId) throws IOException, OperationNotSupportedException {
        target.removeReqCommitRelation(reqId, commitId);
        clearCache();
    }

    /**
     * used for default implementation of getAllReqCommitRelations
     *
     * @param setToCache set should be cached
     */
    protected void cacheReqCommitRelations(SetMultimap<String, String> setToCache) {
        cachedReqCommitRelations = setToCache;
    }

    /**
     * @return cached set of req commit relations
     */
    protected SetMultimap<String, String> getCachedReqCommitRelations() {
        return cachedReqCommitRelations;
    }

    /**
     * @return true if ReqCommitRelations set is cached
     */
    protected boolean isCachedReqCommitRelations() {
        return cachedReqCommitRelations != null;
    }


    /*-------------- Requirement operations --------------*/

    /**
     * try to get cached version of all requirements, creates cached version, if it was was not created before
     *
     * @return set of all requirements, as data objects
     * @throws IOException
     */
    public Set<Requirement> getAllRequirements() throws IOException {
        if (!isCachedAllReqs()) {
            cacheAllReqs(target.getAllRequirements());
        }

        return getCachedAllRequirements();
    }

    @Override
    public void saveOrUpdateRequirement(String id, String title, String description) throws IOException, OperationNotSupportedException {
        target.saveOrUpdateRequirement(id, title, description);
        clearCache();
    }

    /**
     * @return cached requirement or null
     */
    protected Set<Requirement> getCachedAllRequirements() {
        return this.cachedAllRequirements;
    }

    /**
     * put requirements to cache
     */
    protected void cacheAllReqs(Set<Requirement> requirements) {
        this.cachedAllRequirements = requirements;
    }

    /**
     * check if requirements for getAllRequirements() are cached
     *
     * @return true if requirements cached
     */
    protected boolean isCachedAllReqs() {
        return cachedAllRequirements != null;
    }

    /*-------------- Filters operations --------------*/

    /**
     * try to get cached version of all req - commit filters, creates cached version, if it was was not created before
     *
     * @return SetMultimap of req - commit filters
     */
    public SetMultimap<String, String> getReqCommitFilters() {
        if (!isCachedFilters()) {
            cacheReqCommitFilters(target.getReqCommitFilters());
        }
        return getCachedReqCommitFilters();
    }

    @Override
    public void addFilter(String reqId, String commitId) throws OperationNotSupportedException {
        target.addFilter(reqId, commitId);
        clearCache();
    }

    @Override
    public void removeFilter(String reqId, String commitId) throws OperationNotSupportedException {
        target.removeFilter(reqId, commitId);
        clearCache();
    }

    /**
     * saves filters to cache
     *
     * @param filters filter to cache
     */
    protected void cacheReqCommitFilters(SetMultimap<String, String> filters) {
        cachedReqCommitFilters = filters;
    }

    /**
     * @return cached filters collection
     */
    protected SetMultimap<String, String> getCachedReqCommitFilters() {
        return cachedReqCommitFilters;
    }


    /**
     * @return true if filters multimap is cached
     */
    protected boolean isCachedFilters() {
        return cachedReqCommitFilters != null;
    }
}
