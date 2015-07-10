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
package de.fau.osr.core.vcs.base;

import java.io.File;
import java.util.Objects;


/**
 * This entity class holds the properties of a committed file.
 * @author Gayathery
 */

public class CommitFile {

    public CommitState commitState;
    public final File workingCopy;
    public final File oldPath;
    public final File newPath;
    public final String commitID;
    public final String changedData;
    public float impact;

    /**
     * @author Gayathery
     * @param workingCopy the root directory of the working copy of the vcs
     * @param oldPath
     * @param newPath
     * @param commitState
     * @param commitID
     */
    public CommitFile(File workingCopy, File oldPath, File newPath, CommitState commitState, String commitID,String changedData)
    {
    	this.workingCopy = workingCopy;
        this.commitState = commitState;
        this.oldPath = oldPath;
        this.newPath = newPath;
        this.commitID = commitID;
        this.changedData = changedData;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof CommitFile)) return false;
        CommitFile ocf = (CommitFile) o;
        return commitState.equals(ocf.commitState) && oldPath.equals(ocf.oldPath) && newPath.equals(ocf.newPath) && commitID.equals(ocf.commitID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commitState, oldPath, newPath, commitID);
    }
}
