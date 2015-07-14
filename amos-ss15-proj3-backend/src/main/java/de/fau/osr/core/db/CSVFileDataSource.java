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

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.google.common.collect.HashMultimap;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;

/**
 * This <tt>DataSource</tt> is based on CSV file
 * Created by Dmitry Gorelenkov on 03.05.2015.
 */
public class CSVFileDataSource extends DataSource {
    private static final int REQUIREMENT_COLUMN = 0;
    private static final int COMMIT_COLUMN = 1;
    public static final String LINE_END = "\n";
    public static final char ESCAPE_CHAR = '\\';
    public static final Charset CHARSET = Charset.forName("UTF-8");
    public static final char SEPARATOR = ',';
    public static final char QUOTE_CHAR = '\"';

    private File storageFile;

    /**
     * CSV File based data source
     * @param storageFile file to use for data, will be created if not exists
     * @throws IOException in case file is not accessible or cannot be created
     */
    public CSVFileDataSource(File storageFile) throws IOException {
        this.storageFile = storageFile;

        // Create new csv-File if not exists
        if (!storageFile.exists()) {
            if (!storageFile.createNewFile()) {
                throw new IOException("Cannot create data file : " + storageFile.getPath());
            }
        }

    }


    @Override
    public void addReqCommitRelation(String reqId, String commitId) throws IOException {
        try(CSVWriter writer = getWriter()){
            writer.writeNext(new String[]{reqId, commitId});
        }
    }

    @Override
    public void removeReqCommitRelation(String reqId, String commit) throws IOException {
        throw new UnsupportedOperationException(); //TODO
    }

    @Override
    public HashSet<String> getCommitRelationByReq(String reqId) throws IOException {
        return findBy(reqId, REQUIREMENT_COLUMN, COMMIT_COLUMN);
    }

    @Override
    public HashSet<String> getReqRelationByCommit(String commitId) throws IOException {
        return findBy(commitId, COMMIT_COLUMN, REQUIREMENT_COLUMN);
    }

    @Override
    public HashMultimap<String, String> getAllReqCommitRelations() throws IOException {
        HashMultimap<String, String> relations = HashMultimap.create();

        try (CSVReader reader = getReader()) {
            List<String[]> allLines = reader.readAll();
            String reqId;
            String commitId;
            for (String[] line : allLines) {
                reqId = line[REQUIREMENT_COLUMN];
                commitId = line[COMMIT_COLUMN];
                relations.put(reqId, commitId);
            }

        }
        return relations;
    }

    /**
     * lookup for values match in csv file, line by line.
     *
     * @param searchValue      value to search for
     * @param searchByColumn   column to compare search values
     * @param resultFromColumn column to pick values from
     * @return set of values of {@code resultFromColumn} column, filtered by {@code searchValue} in {@code searchByColumn}
     * @throws IOException TODO multiple return column values
     */
    private HashSet<String> findBy(String searchValue, int searchByColumn, int resultFromColumn) throws IOException {
        HashSet<String> commits = new HashSet<>();

        try (CSVReader reader = getReader()) {
            List<String[]> allLines = reader.readAll();

            for (String[] line : allLines) {
                //if reqId is the same, add to founded commits
                if (line[searchByColumn].equals(searchValue)) {
                    commits.add(line[resultFromColumn]);
                }
            }

        }

        return commits;
    }


    private CSVReader getReader() throws FileNotFoundException {
        InputStreamReader reader = new InputStreamReader(new FileInputStream(storageFile), CHARSET);
        return new CSVReader(reader, SEPARATOR, QUOTE_CHAR, ESCAPE_CHAR);
    }

    private CSVWriter getWriter() throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(storageFile, true), CHARSET);
        return new CSVWriter(writer, SEPARATOR, QUOTE_CHAR, ESCAPE_CHAR, LINE_END);
    }
}
