package de.fau.osr.core.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

/**
 * @author Taleh Didover
 *
 * Uses CSV file as db.
 */
public class CSVFileReqCommitRelationDB implements ReqCommitRelationDB {
    Path storagePath;
    Map<Integer, Set<String>> commitIDsByReqID;


    public CSVFileReqCommitRelationDB(Path storagePath) {
        this.storagePath = storagePath;
    }

    @Override
    public void addFurtherDependency(Integer reqID, Set<String> commitIDs) {
    }

    @Override
    public void addFurtherDependency(Integer reqID, String commitID) {

    }

    @Override
    public Map<Integer, Set<String>> getDependencies() {
        return null;
    }
}
