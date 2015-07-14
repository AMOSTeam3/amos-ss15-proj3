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
import de.fau.osr.core.Requirement;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * uses filters to save / get information for relations
 * Created by Dmitry Gorelenkov on 14.07.2015.
 * todo create and use some FilterSource class instead of filter methods in DataSource
 */
public class FilteringCompositeDataSource extends CompositeDataSource {


    public FilteringCompositeDataSource(DataSource mainSource, DataSource... otherSources) {
        super(mainSource, otherSources);
    }

    @Override
    public void addReqCommitRelation(String reqId, String commitId) throws IOException, OperationNotSupportedException {
        //add relation, throws exception if already exists, as usual
        super.addReqCommitRelation(reqId, commitId);

        //if such relation exists in filters, remove filter
        removeFilter(reqId, commitId);
    }

    @Override
    public void removeReqCommitRelation(String reqId, String commitId) throws IOException, OperationNotSupportedException {
        //if such relation exists, remove it
        super.removeReqCommitRelation(reqId, commitId);
        //create filter
        addFilter(reqId, commitId);
    }

    @Override
    public SetMultimap<String, String> getAllReqCommitRelations() throws IOException {
        //copy of original set
        SetMultimap<String, String> toFilter = HashMultimap.create(super.getAllReqCommitRelations());

        SetMultimap<String, String> filters = getReqCommitFilters();

        //for each filter, try to remove such relation from original collection
        for (Map.Entry<String, String> reqCommitRelation : filters.entries()) {
            toFilter.remove(reqCommitRelation.getKey(), reqCommitRelation.getValue());
        }

        return toFilter;
    }

    @Override
    public Set<Requirement> getAllRequirements() throws IOException {
        Set<Requirement> origReqs = super.getAllRequirements();

        SetMultimap<String, String> filters = getReqCommitFilters();

        for (Requirement req : origReqs) {
            //get filtered commits
            Set<String> filtered = filters.get(req.getId());
            //remove them from req
            req.getCommitIds().removeAll(filtered);
        }


        return origReqs;
    }

}
