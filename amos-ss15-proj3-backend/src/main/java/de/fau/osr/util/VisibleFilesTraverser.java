package de.fau.osr.util;


import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;


import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.*;

/**
 * Returns all files paths of a given directory
 * but ignores all hidden files and directories
 * and files and directories with certain file names.
 *
 * @author Taleh Didover
 */
public class VisibleFilesTraverser extends DirectoryWalker<File> {
    File startDirectory;
    String[] ignoreList;

    /**
     * Builds a *VisibleFilesTraverser*.
     * @param startDirectory
     * @param ignoreFileNames
     * @return
     */
    public static VisibleFilesTraverser Get(String startDirectory, String...ignoreFileNames) {
        return new VisibleFilesTraverser(
                new File(startDirectory),
                ignoreFileNames,
                FileFilterUtils.or(
                        // Show visible directories
                        FileFilterUtils.and(FileFilterUtils.directoryFileFilter(), HiddenFileFilter.VISIBLE),
                        // Show visible files
                        FileFilterUtils.and(FileFilterUtils.fileFileFilter(), HiddenFileFilter.VISIBLE)
                )
        );
    }

    private VisibleFilesTraverser(File startDirectory, String[] ignoreFilenames, FileFilter filter) {
        super(filter, -1);
        this.startDirectory = startDirectory;
        this.ignoreList = ignoreFilenames;

    }

    public Collection<File> traverse() throws IOException {
        Collection<File> alreatyFoundFiles = new ArrayList<>();
        walk(startDirectory, alreatyFoundFiles);
        return alreatyFoundFiles;
    }


    protected boolean handleDirectory(File dir, int depth, Collection results) {
        String dirname = dir.getName();
        for (String eachIgnore: ignoreList)
            if (eachIgnore.equals(dirname))
                return false;
        return true;
    }

    protected void handleFile(File file, int depth, Collection results) {
        String filename = file.getName();
        for (String eachIgnore: ignoreList)
            if (eachIgnore.equals(filename))
                return;
        results.add(file);
    }
}
