package de.fau.osr.core.db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

/**
 * @author Taleh Didover
 *
 * Uses CSV file as db.
 */
public class CSVFileReqCommitRelationDB implements ReqCommitRelationDB {
    Path storagePath;
    final Charset CHARSET = Charset.forName("US-ASCII");
    final String CSV_DELIMITER = ",";
    final String COMMENTARY_SYMBOL = "#";
    Map<Integer, Set<String>> commitIDsByReqID;

    public CSVFileReqCommitRelationDB(Path storagePath) {
        this.storagePath = storagePath;

        // Create new csv-File and csv-Header
        boolean isEmptyFile = true;

        if (storagePath.toFile().exists()) {
            try (BufferedReader reader = Files.newBufferedReader(storagePath, CHARSET)) {
                if (reader.readLine() != null)
                    isEmptyFile = false;
            } catch (IOException x) {
                System.err.format("IOException: %s%n", x);
            }
        }

        if (isEmptyFile) {
            try (BufferedWriter writer = Files.newBufferedWriter(storagePath, CHARSET)) {

                String s = String.format("%sreq-id%scommit-id", COMMENTARY_SYMBOL, CSV_DELIMITER);
                writer.write(s, 0, s.length());
            } catch (IOException x) {
                System.err.format("IOException: %s%n", x);
                System.exit(1);
            }
        }

    }

    @Override
    public void addFurtherDependency(Integer reqID, Set<String> commitIDs) {
        for (String commitID : commitIDs)
            addFurtherDependency(reqID, commitID);
    }

    @Override
    public void addFurtherDependency(Integer reqID, String commitID) {
        // https://docs.oracle.com/javase/tutorial/essential/io/file.html
        try (BufferedWriter writer = Files.newBufferedWriter(storagePath, CHARSET, StandardOpenOption.APPEND)) {
            String s = String.format("%d%s%s%n", reqID, CSV_DELIMITER, commitID);
            writer.write(s, 0, s.length());
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }

    @Override
    public Map<Integer, Set<String>> getDependencies() {
		Map<Integer, Set<String>> reqCommitRel = new HashMap<Integer, Set<String>>();
        // https://docs.oracle.com/javase/tutorial/essential/io/file.html
        Charset charset = Charset.forName("US-ASCII");
        try (BufferedReader reader = Files.newBufferedReader(storagePath, charset)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                String actualLine = new ArrayDeque<String>(Arrays.asList(line.split(COMMENTARY_SYMBOL))).removeLast();
                if (actualLine.isEmpty())
                    continue;

				String[] splittedLine = actualLine.split(CSV_DELIMITER);

                Integer reqID = Integer.valueOf(splittedLine[0]);
                String commitID = splittedLine[1];

                if (!reqCommitRel.containsKey(reqID))
                    reqCommitRel.put(reqID, new HashSet<String>());
                reqCommitRel.get(reqID).add(commitID);
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }

        return reqCommitRel;
    }
}
