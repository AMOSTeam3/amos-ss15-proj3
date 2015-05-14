package de.fau.osr.core.db;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Dmitry Gorelenkov on 13.05.2015.
 */
public class CompositeDataSource extends DataSource {

    private DataSource dataSource;
    private ArrayList<DataSource> dataSources;

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
    public void addReqCommitRelation(String reqId, String commitId) throws IOException, OperationNotSupportedException {
        dataSource.addReqCommitRelation(reqId, commitId);
    }

    @Override
    public void removeReqCommitRelation(String reqId, String commitId) throws IOException, OperationNotSupportedException {
        dataSource.removeReqCommitRelation(reqId, commitId);
    }

    @Override
    public SetMultimap<String, String> getAllReqCommitRelations() throws IOException {
        SetMultimap<String, String> result = HashMultimap.create();
        for (DataSource ds : dataSources){
            result.putAll(ds.getAllReqCommitRelations());
        }

        return result;
    }
}
