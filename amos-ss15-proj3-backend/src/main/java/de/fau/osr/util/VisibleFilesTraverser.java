package de.fau.osr.util;


import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;


import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * Returns all files paths of a given directory
 * but ignores all hidden files and directories
 * and files and directories with certain file names.
 *
 * @author Taleh Didover
 */
public class VisibleFilesTraverser extends DirectoryWalker<Path> {
    Path startDirectory;
    String[] ignoreList;

    /**
     * Builds a *VisibleFilesTraverser*.
     * @param startDirectory
     * @param ignoreFileNames
     * @return
     */
    public static VisibleFilesTraverser Get(Path startDirectory, String...ignoreFileNames) {
        return new VisibleFilesTraverser(
                startDirectory,
                ignoreFileNames,
                FileFilterUtils.or(
                        // Show visible directories
                        FileFilterUtils.and(FileFilterUtils.directoryFileFilter(), HiddenFileFilter.VISIBLE),
                        // Show visible files
                        FileFilterUtils.and(FileFilterUtils.fileFileFilter(), HiddenFileFilter.VISIBLE)
                )
        );
    }

    private VisibleFilesTraverser(Path startDirectory, String[] ignoreFilenames, FileFilter filter) {
        super(filter, -1);
        this.startDirectory = startDirectory;
        this.ignoreList = ignoreFilenames;

    }

    /**
     * Returns paths of all filtered files. Paths are relative to *startDirectory*.
     *
     * @return
     * @throws IOException
     */

    public Collection<Path> traverse() throws IOException {
        Collection<Path> alreatyFoundFiles = new ArrayList<>();
        walk(startDirectory.toFile(), alreatyFoundFiles);
        return alreatyFoundFiles;
    }


    protected boolean handleDirectory(File dir, int depth, Collection results) {
        String dirname = dir.getName();
        for (String eachIgnore: ignoreList)
            if (dirname.contains(eachIgnore))
                return false;
        return true;
    }

    protected void handleFile(File file, int depth, Collection results) {
        String filename = file.getName();
        for (String eachIgnore: ignoreList)
            if (filename.contains(eachIgnore))
                return;
        results.add(startDirectory.relativize(file.toPath()));
    }
}
