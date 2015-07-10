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
package de.fau.osr.core.vcs.impl;

import com.google.common.collect.Lists;

import de.fau.osr.core.db.DataSource;
import de.fau.osr.core.vcs.AnnotatedLine;
import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.CommitState;
import de.fau.osr.core.vcs.interfaces.VcsClient;

import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revplot.PlotCommit;
import org.eclipse.jgit.revplot.PlotCommitList;
import org.eclipse.jgit.revplot.PlotLane;
import org.eclipse.jgit.revplot.PlotWalk;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.AndTreeFilter;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.treewalk.filter.TreeFilter;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.stream.Stream;

/**
 * VCS Client implementation for Git
 * @author Gayathery
 */
public class GitVcsClient extends VcsClient{

    Git git;
    Repository repo;

    /**
     * @param repositoryURI
     * @author Gayathery
     * @throws IOException
     */
    public GitVcsClient(String repositoryURI) throws IOException
    {
        super(repositoryURI);
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        repo = builder.setGitDir(new File(repositoryURI)).setMustExist(true).build();
        git = new Git(repo);
    }

    /* (non-Javadoc)
     * @see de.fau.osr.core.vcs.interfaces.VcsClient#getBranchList()
     * @author Gayathery
     */
    @Override
    public Iterator<String> getBranchList() {
        ArrayList<String> branchList = new ArrayList<String>();
        try {
            List<Ref> branches = git.branchList().call();

            for (Ref branch : branches) {
                branchList.add(branch.getName());
            }
        } catch (GitAPIException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return branchList.iterator();
    }


    /* (non-Javadoc)
     * @see de.fau.osr.core.vcs.interfaces.VcsClient#getCommitList()
     * @author Gayathery
     */
    @Override
    public Iterator<String> getCommitList() {
        try {
            final Iterator<RevCommit> commits = git.log().all().call().iterator();
            return new Iterator<String>() {
                @Override
                public boolean hasNext() {
                    return commits.hasNext();
                }

                @Override
                public String next() {
                    return commits.next().getName();
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }

            };

        } catch (GitAPIException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    private Stream<CommitFile> getTreeDiffFiles(RevTree a, RevTree b, String commitID) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DiffFormatter dif = new DiffFormatter(out);
        dif.setRepository(repo);
        dif.setDiffComparator(RawTextComparator.DEFAULT);
        dif.setDetectRenames(true);
        List<DiffEntry> diffs =  dif.scan(a, b);
        return diffs.stream().map( diff -> {
            CommitState commitState;
            switch(diff.getChangeType())
            {
            case ADD:
                commitState = CommitState.ADDED;
                break;
            case MODIFY:
                commitState = CommitState.MODIFIED;
                break;
            case RENAME:
                commitState = CommitState.RENAMED;
                break;
            case DELETE:
                commitState = CommitState.DELETED;
                break;
            case COPY:
                commitState = CommitState.COPIED;
                break;
            default:
                throw new RuntimeException("Encountered an unknown DiffEntry.ChangeType " + diff.getChangeType() + ". Please report a bug.");
            }
            try {
				dif.format(diff);
			} catch (Exception e1) {
				throw new Error(e1);
			}
            diff.getOldId();
            String changedData = "";
              try {
                  changedData = out.toString("UTF-8");
                  out.reset();

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            File newPath = new File(diff.getNewPath());
            File oldPath = new File(diff.getOldPath());
            if(commitState == CommitState.DELETED)
            	newPath = oldPath;
            CommitFile commitFile = new CommitFile(getWorkingCopy(), new File(diff.getOldPath()), newPath, commitState, commitID, changedData);
            LoggerFactory.getLogger(getClass()).debug(
                    MessageFormat.format("({0} {1} {2})",
                            diff.getChangeType().name(),
                            diff.getNewMode().getBits(),
                            diff.getNewPath()));
            return commitFile;
        });
        }

    /* (non-Javadoc)
     * @see de.fau.osr.core.vcs.interfaces.VcsClient#getCommitListForFileodification(java.lang.String)
     * @author Gayathery
     */
    public Iterator<String> getCommitListForFileodification(String path){
        PlotCommitList<PlotLane> plotCommitList = new PlotCommitList<PlotLane>();
        PlotWalk revWalk = new PlotWalk(repo);
        ArrayList<String> commitIDList = new ArrayList<String>();
        try {

            ObjectId rootId = repo.resolve("HEAD");
            if (rootId != null) {
                RevCommit root = revWalk.parseCommit(rootId);
                revWalk.markStart(root);
                revWalk.setTreeFilter(
                        AndTreeFilter.create(PathFilter.create(path), TreeFilter.ANY_DIFF));
                plotCommitList.source(revWalk);
                plotCommitList.fillTo(Integer.MAX_VALUE);

                Iterator<PlotCommit<PlotLane>> commitListIterator = plotCommitList.iterator();
                while(commitListIterator.hasNext())
                {
                    commitIDList.add(commitListIterator.next().getName());
                }
                return commitIDList.iterator();
            }

        } catch (AmbiguousObjectException ex) {

        } catch (IOException ex) {

        }
        return commitIDList.iterator();
    }

    /* (non-Javadoc)
     * @see de.fau.osr.core.vcs.interfaces.VcsClient#getCommitFiles(java.lang.String)
     * @author Gayathery
     */
    @Override
    public Supplier<Stream<CommitFile>> getCommitFiles(String commitID) {
    	return () -> {
    		Stream res = Stream.empty();
    		try {
    			Repository repo = git.getRepository();
    			ObjectId obj = repo.resolve(commitID);
    			RevWalk revWalk = new RevWalk(repo);
    			RevCommit commit = revWalk.parseCommit(obj);
    			RevCommit[] parents = commit.getParents();
    			if(parents.length == 0) {
    				return getTreeDiffFiles(commit.getTree(), null,commit.getName());
    			}
    			res = Stream.empty();
    			for(RevCommit parent : parents) {
    				revWalk.parseBody(parent);
    				res = Stream.concat(res, getTreeDiffFiles(parent.getTree(), commit.getTree(), commit.getName()));
    			}
    		} catch (IOException e1) {

    			e1.printStackTrace();
    		} catch (ArrayIndexOutOfBoundsException e1) {

    			e1.printStackTrace();
    		}
    		return res;
    	};
    }

    /* (non-Javadoc)
     * @see de.fau.osr.core.vcs.VcsClient#getCommitMessage(java.lang.String)
     * @author Florian Gerdes
     */
    @Override
    public String getCommitMessage(String commitID) {
        RevCommit commit = null;
        try {
            Repository repo = git.getRepository();
            ObjectId obj = repo.resolve(commitID);
            commit = (new RevWalk(repo)).parseCommit(obj);
        } catch (IOException e1) {

            e1.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e1) {

            e1.printStackTrace();
        }
        return commit.getFullMessage();
    }
    
    /**
     * @param walker
     * @param path the relative path of the file to fetch
     * @param commit has to belong to the passed RevWalk
     * @return null if path does not exist in this revision, its ObjectId otherwise
     * @throws IOException
     * @throws GitAPIException
     */
    ObjectId fileAtRev(RevWalk walker, String path, RevCommit commit) throws GitAPIException, IOException {
    	RevTree tree = commit.getTree();
    	TreeWalk treeWalk = TreeWalk.forPath(git.getRepository(), path, tree);
    	if(treeWalk == null) return null;
    	ObjectId file = treeWalk.getObjectId(0);
    	return file;
    }
    
    /**
     * Reads object into a byte array.
     * @param object
     * @return The bytes of object interpreted as utf8 String
     * @throws GitAPIException
     * @throws IOException
     */
    byte[] readBlob(ObjectId object) throws GitAPIException, IOException {
    	ObjectReader reader = git.getRepository().newObjectReader();
    	return reader.open(object).getBytes();
    }
    
    /**
     * This method returns the current repository name.This method is used in setting the file name for file export options.
     * @return
     */
    @Override
    public String getRepositoryName(){
            StoredConfig c = repo.getConfig();
        try{
            String url = c.getString("remote", "origin","url");
            if(url!= null && !url.isEmpty()){
                return url.substring(url.lastIndexOf('/') +1);
            }
        }catch(Exception e){
            e.printStackTrace();

        }
        return ".git";
    }
}
