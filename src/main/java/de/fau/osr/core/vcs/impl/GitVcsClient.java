package de.fau.osr.core.vcs.impl;

import com.beust.jcommander.internal.Lists;
import de.fau.osr.core.db.DataSource;
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
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revplot.PlotCommit;
import org.eclipse.jgit.revplot.PlotCommitList;
import org.eclipse.jgit.revplot.PlotLane;
import org.eclipse.jgit.revplot.PlotWalk;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
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

/**
 * @author Gayathery
 * @desc VCS Client implementation for Git
 *
 */
public class GitVcsClient extends VcsClient{

	Git git;
	String repositoryURI;
	Repository repo;
	
	/**
	 * @param repositoryURI
	 * @author Gayathery
	 * @throws IOException 
	 */
	public GitVcsClient(String repositoryURI) throws IOException
	{
		this.repositoryURI = repositoryURI;
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

	
	private void getTreeDiffFiles(RevTree a, RevTree b, ArrayList<CommitFile> s, String commitID) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DiffFormatter dif = new DiffFormatter(out);
		dif.setRepository(repo);
		dif.setDiffComparator(RawTextComparator.DEFAULT);
		dif.setDetectRenames(true);
		List<DiffEntry> diffs =  dif.scan(a, b);

		for (DiffEntry diff : diffs) {
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
			dif.format(diff);
			diff.getOldId();
			String changedData = "";
		      try {
		    	  changedData = out.toString("UTF-8");
		    	  out.reset();
				 
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			CommitFile commitFile = new CommitFile(new File(diff.getOldPath()),new File(diff.getNewPath()),commitState,commitID,changedData);
			if(commitFile.commitState == CommitState.DELETED)
				commitFile.newPath = commitFile.oldPath;
			s.add(commitFile);
			LoggerFactory.getLogger(getClass()).debug(
					MessageFormat.format("({0} {1} {2})",
							diff.getChangeType().name(),
							diff.getNewMode().getBits(),
							diff.getNewPath()));
		}
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
	public ArrayList<CommitFile> getCommitFiles(String commitID) {

		ArrayList<CommitFile> commitFilesList = new ArrayList<>();

		try {
			Repository repo = git.getRepository();
			ObjectId obj = repo.resolve(commitID);
			RevWalk revWalk = new RevWalk(repo);
			RevCommit commit = revWalk.parseCommit(obj);
			RevCommit[] parents = commit.getParents();
			if(parents.length == 0) {
				getTreeDiffFiles(commit.getTree(), null, commitFilesList,commit.getName());
			}
			for(RevCommit parent : parents) {
				revWalk.parseBody(parent);
				getTreeDiffFiles(parent.getTree(), commit.getTree(), commitFilesList,commit.getName());
			}
		} catch (IOException e1) {

			e1.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e1) {

			e1.printStackTrace();
		}

		return commitFilesList;
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
	
	
	/* (non-Javadoc)
	 * @see de.fau.osr.core.vcs.interfaces.VcsClient#blame(java.lang.String, de.fau.osr.util.parser.CommitMessageParser)
	 */
	@Override
	public List<AnnotatedLine> blame(String path, DataSource dataSource) throws IOException, GitAPIException {
		BlameCommand blameCommand = new BlameCommand(git.getRepository());
		blameCommand.setFollowFileRenames(true);
		blameCommand.setFilePath(path);
		BlameResult blameResult = blameCommand.call();
		ArrayList<AnnotatedLine> res = new ArrayList<>();
		if(blameResult == null) throw new FileNotFoundException(path);
		blameResult.computeAll();
		RawText text = blameResult.getResultContents();
		for(int i=0; i<text.size(); ++i) {
			//String commitId = blameResult.getSourceCommit(res.size()).getName();
			//TODO add abstract linkage source here
			List<String> annotation;
			RevCommit commit = blameResult.getSourceCommit(res.size());
			if(commit != null)
				annotation = Lists.newArrayList(dataSource.getReqRelationByCommit(blameResult.getSourceCommit(res.size()).getName()));
			else
				annotation = Collections.emptyList();
			res.add(new AnnotatedLine(annotation, text.getString(i)));
		}
		return res;
	}
}
