package de.fau.osr.core.vcs.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.CommitState;
import de.fau.osr.core.vcs.interfaces.VcsClient;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.util.io.DisabledOutputStream;

/**
 * @author Gayathery
 * @desc VCS Client implementation for Git
 *
 */
public class GitVcsClient implements VcsClient{

	Git git;
	String repositoryURI;
	Repository repo;
	Boolean isConnected;
	
	/**
	 * @param repositoryURI
	 * @author Gayathery
	 */
	public GitVcsClient(String repositoryURI)
	{
		this.repositoryURI = repositoryURI;
		isConnected = false;
		repo = null;
		git = null;
	}
	
	
	/* (non-Javadoc)
	 * @see org.amos.core.vcs.interfaces.VcsClient#connect()
	 * @author Gayathery
	 */
	
	public boolean connect()
	{
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		try {
			repo = builder.setGitDir(new File(repositoryURI)).setMustExist(true).build();
			git = new Git(repo);
		} catch (IOException e) {
			e.printStackTrace();
			return isConnected;
		}
		
		isConnected = true;
		return isConnected;
	}
	
	/* (non-Javadoc)
	 * @see org.amos.core.vcs.interfaces.VcsClient#getBranchList()
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
	 * @see org.amos.core.vcs.interfaces.VcsClient#getCommitList()
	 * @author Gayathery
	 */
	@Override
	public Iterator<String> getCommitList() {
		ArrayList<String> commitList = new ArrayList<String>();
		
		try {
			Iterable<RevCommit> commits = git.log().all().call();
			for (RevCommit commit : commits) {
	        	commitList.add(commit.getId().getName());
	        }
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return commitList.iterator();
	}
	
	
	/* (non-Javadoc)
	 * @see org.amos.core.vcs.interfaces.VcsClient#getCommitFiles(java.lang.String)
	 * @author Gayathery
	 */
	@Override
	public Iterator<CommitFile> getCommitFiles(String commitID) {
		
		ArrayList<CommitFile> commitFilesList = new ArrayList<CommitFile>();
		
		try {
			Iterable<RevCommit> commits = git.log().all().call();
			for (RevCommit commit : commits) {
	        	if(!commit.getId().getName().equals(commitID))
	        		continue;
	        	
	        	RevCommit parent = commit.getParent(0);
				
	        	DiffFormatter dif = new DiffFormatter(DisabledOutputStream.INSTANCE);
	        	dif.setRepository(repo);
	        	dif.setDiffComparator(RawTextComparator.DEFAULT);
	        	dif.setDetectRenames(true);
	        	List<DiffEntry> diffs =  dif.scan(parent.getTree(), commit.getTree());
				
	        	for (DiffEntry diff : diffs) {
	        		CommitState commitState;
	        		commitState = CommitState.NOTSET;
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
	        			default:
	        				break;
	        		}
	        		CommitFile commitFile = new CommitFile(new File(diff.getOldPath()),new File(diff.getNewPath()),commitState);
	        	    commitFilesList.add(commitFile);
	        		//System.out.println(MessageFormat.format("({0} {1} {2}", diff.getChangeType().name(), diff.getNewMode().getBits(), diff.getNewPath()));
	        }
	        
			
		}
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		catch (GitAPIException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
		}catch (ArrayIndexOutOfBoundsException e1) {
			
			e1.printStackTrace();
		}
		
		return commitFilesList.iterator();
}
}
