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
import com.google.common.collect.SetMultimap;
import de.fau.osr.core.Requirement;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This <tt>DataSource</tt> uses multiple <tt>DataSource</tt>'s
 * Created by Dmitry Gorelenkov on 13.05.2015.
 */
public class CompositeDataSource extends DataSource {

    private DataSource dataSource;
    private ArrayList<DataSource> dataSources;

    /**
     * <tt>DataSource</tt>, that uses many <tt>DataSource</tt>'s for query data, but only one for update
     * @param mainSource source to use modifying methods like addSource()
     * @param otherSources other source, to query information
     */
    public CompositeDataSource(DataSource mainSource, DataSource ...otherSources ) {
        dataSources = new ArrayList<>();
        dataSource = mainSource;
        addSource(mainSource);
        addSources(otherSources);
    }

    public void addSource(DataSource ds){
        dataSources.add(ds);
    }

    public void addSources(DataSource ...manySources){
        dataSources.addAll(Arrays.asList(manySources));
    }


    @Override
    protected void doAddReqCommitRelation(String reqId, String commitId) throws IOException, OperationNotSupportedException {
        dataSource.addReqCommitRelation(reqId, commitId);
    }

    @Override
    protected void doRemoveReqCommitRelation(String reqId, String commitId) throws IOException, OperationNotSupportedException {
        dataSource.removeReqCommitRelation(reqId, commitId);
    }

    @Override
    protected void doSaveOrUpdateRequirement(String id, String title, String description) throws IOException, OperationNotSupportedException {
        //use main data source for update
        dataSource.saveOrUpdateRequirement(id, title, description);
    }

    @Override
    protected Set<Requirement> doGetAllRequirements() throws IOException {

        Set<Requirement> result = new HashSet<>();
        for (DataSource ds : dataSources) {
            result.addAll(ds.getAllRequirements());
        }

        return result;
    }

    @Override
    protected SetMultimap<String, String> doGetAllReqCommitRelations() throws IOException {
        SetMultimap<String, String> result = HashMultimap.create();
        for (DataSource ds : dataSources){
            result.putAll(ds.getAllReqCommitRelations());
        }

        return result;
    }
}
