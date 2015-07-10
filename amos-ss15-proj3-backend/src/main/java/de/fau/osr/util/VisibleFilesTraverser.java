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

    protected void handleFile(File file, int depth, Collection results) throws IOException {
        String filename = file.getCanonicalPath();
        for (String eachIgnore: ignoreList)
            if (filename.contains(eachIgnore))
                return;
        results.add(startDirectory.relativize(file.toPath()));
    }
}
