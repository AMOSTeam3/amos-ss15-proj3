package de.fau.osr.core.db;

import java.util.Map;
import java.util.Set;

/**
 * @author Taleh Didover
 *
 * This defines a simple interface to store and load
 * relation information of requirement ids with commit ids.
 */
public interface ReqCommitRelationDB {
    void addFurtherDependency(Integer reqID, Set<String> commitIDs);
    void addFurtherDependency(Integer reqID, String commitID);
    Map<Integer, Set<String>> getDependencies();
    //TODO Will we need a method, which returns an Iterator?
}
