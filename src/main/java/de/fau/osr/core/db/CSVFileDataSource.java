package de.fau.osr.core.db;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;

/**
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
//    public final String COMMENTARY_SYMBOL = "#";

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
    void addReqCommitRelation(Integer reqId, String commitId) throws IOException {
        try(CSVWriter writer = getWriter()){
            writer.writeNext(new String[]{reqId.toString(), commitId});
        }
    }

    @Override
    void removeReqCommitRelation(Integer reqId, String commit) throws Exception {
        throw new NotImplementedException(); //TODO
    }

    @Override
    HashSet<String> getCommitRelationByReq(Integer reqId) throws IOException {
        return findBy(reqId.toString(), REQUIREMENT_COLUMN, COMMIT_COLUMN);
    }

    @Override
    HashSet<Integer> getReqRelationByCommit(String commitId) throws IOException {

        HashSet<String> reqsAsString = findBy(commitId, COMMIT_COLUMN, REQUIREMENT_COLUMN);
        //todo remove if we use reqId as string
        HashSet<Integer> reqsAsInts = new HashSet<>();
        for (String req : reqsAsString) {
            reqsAsInts.add(Integer.parseInt(req));
        }

        return reqsAsInts;
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
