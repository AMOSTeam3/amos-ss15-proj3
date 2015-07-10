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
package de.fau.osr.core.vcs.interfaces;

import de.fau.osr.core.db.DataSource;
import de.fau.osr.core.vcs.AnnotatedLine;
import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.VcsEnvironment;
import de.fau.osr.core.vcs.impl.GitVcsClient;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Gayathery
 * 
 */
public abstract class VcsClient {

    protected final String repositoryURI;
    protected VcsClient(String repositoryURI) {
    	this.repositoryURI = repositoryURI;
    }

	public abstract Iterator<String> getBranchList();
    public abstract Iterator<String> getCommitList();
    public abstract String getRepositoryName();
    public abstract Supplier<Stream<CommitFile>> getCommitFiles(String commitID);
    public abstract String getCommitMessage(String commitID);
    public abstract Iterator<String> getCommitListForFileodification(String filePath);
    public static VcsClient connect(VcsEnvironment env, String repositoryURI) {
        VcsClient client;
        try {
            switch(env) {
            case GIT:
                client = new GitVcsClient(repositoryURI);
                break;
            default:
                throw new RuntimeException("unknown vcs environment " + env);
            }
        } catch(RepositoryNotFoundException e){
            throw new RuntimeException("Repository Not Found");
        }catch(IOException e) {
            throw new RuntimeException(e);
        }
        return client;
    }
    
    /**
     * Get the root path of the working copy of this vcs repository.
     * Does not work with bare repositories.
     */
    public File getWorkingCopy() {
    	/* Currently the assumption that the working copy is always just the
    	 * parent of the repositoryURI is correct for the only vcs we handle, git.
    	 * This might have to be changed for other vcses
    	 */
    	return new File(repositoryURI).getParentFile(); 
    }
}
