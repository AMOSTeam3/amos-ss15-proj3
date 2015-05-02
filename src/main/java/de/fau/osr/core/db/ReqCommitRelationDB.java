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
    Iterable<String> getDependencies(Integer reqID);
    Iterable<Integer> getDependencies(String commitID);
}
