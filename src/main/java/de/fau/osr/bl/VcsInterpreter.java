package de.fau.osr.bl;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.CommitState;
import de.fau.osr.core.vcs.base.VcsController;
import de.fau.osr.util.parser.CommitMessageParser;
import fj.Ord;
import fj.P2;
import fj.data.Array;
import fj.data.Option;
import fj.data.Set;
import fj.data.Tree;
import fj.data.TreeMap;
/**
 * This class tracks the relationship between files and requirements. It
 * internally keeps all information for all commits in memory. This leads to
 * efficient incremental updates if commits are added.
 * It assumes that the content of a commit with a constant commitID cannot change,
 * which is a valid assumption for git. If a commit were to change without
 * changing its commitID the internal metadata would become stale and the
 * VcsInterpreter should be recreated.
 * @author Gayathery
 *
 */
public class VcsInterpreter {

	VcsController vcsController;

	public VcsInterpreter(VcsController vcsController) {
		this.vcsController = vcsController;
	}

	Map<String,TreeMap<Integer,Set<File>>> commitToReqToFiles = new HashMap<String,TreeMap<Integer,Set<File>>>();
	Map<String,TreeMap<File,Set<Integer>>> commitToFileToReqs = new HashMap<String,TreeMap<File,Set<Integer>>>();

	/* (non-Javadoc)
	 * @see de.fau.osr.bl.VcsInterpreter#getCommitFilesForRequirementID(java.lang.String)
	 * @author Gayathery
	 */
	public Iterable<File> getCommitFilesForRequirementID(Integer requirementID)
	{
		Tree<String> history = vcsController.getCommitTree("HEAD");
		makeAvailable(history);
		Option<Set<File>> opt = commitToReqToFiles.get(history.root()).get(requirementID);
		if(opt.isSome()) return opt.some();
		return Collections.emptyList();
	}
	
	public Iterable<Integer> getRequirementsForFile(File file) {
		Tree<String> history = vcsController.getCommitTree("HEAD");
		makeAvailable(history);
		Option<Set<Integer>> opt = commitToFileToReqs.get(history.root()).get(file);
		if(opt.isSome()) return opt.some();
		return Collections.emptyList();
	}

	/**
	 * @param history
	 * After calling this method, the internal maps are guaranteed to contain
	 * all commitIDs in history.
	 */
	private void makeAvailable(Tree<String> history) {
		final Ord<File> FILE_ORDER = Ord.<File>comparableOrd();
		final Ord<Integer> INT_ORDER = Ord.<Integer>comparableOrd();
		if(commitToReqToFiles.containsKey(history.root())) return;
		Array<Tree<String>> subtrees = history.subForest()._1().toArray();
		commitToReqToFiles.put(history.root(), TreeMap.<Integer,Set<File>>empty(INT_ORDER));
		commitToFileToReqs.put(history.root(), TreeMap.<File,Set<Integer>>empty(FILE_ORDER));
		List<Integer> reqs = new CommitMessageParser().parse(vcsController.getCommitMessage(history.root()));
		if(subtrees.length() == 0) {
			// this is the initial commit, add all files
			for(Iterator<CommitFile> it = vcsController.getCommitFiles(history.root()); it.hasNext();) {
				CommitFile file = it.next();
				commitToFileToReqs.put(history.root(), commitToFileToReqs.get(history.root()).set(file.newPath, Set.empty(INT_ORDER)));
				for(Integer req : reqs) {
					if(!commitToReqToFiles.get(history.root()).contains(req)) {
						commitToReqToFiles.put(history.root(),
								commitToReqToFiles.get(history.root()).set(req, Set.single(FILE_ORDER, file.newPath)));
					} else {
						commitToReqToFiles.put(history.root(),
								commitToReqToFiles.get(history.root()).set(req,
										commitToReqToFiles.get(history.root()).get(req).some().insert(file.newPath)));
					}
					commitToFileToReqs.put(history.root(),
							commitToFileToReqs.get(history.root()).set(file.newPath,
									commitToFileToReqs.get(history.root()).get(file.newPath).some().insert(req)));
				}
			}
		}
		for(Tree<String> child : subtrees) {
			makeAvailable(child);
			Iterable<CommitFile> diff = vcsController.getDiff(child.root(), history.root());
			TreeMap<Integer, Set<File>> nextReqToFile = commitToReqToFiles.get(child.root());
			TreeMap<File, Set<Integer>> nextFileToReq = commitToFileToReqs.get(child.root());
			for(CommitFile file : diff) {
				if(file.commitState != CommitState.DELETED && !nextFileToReq.contains(file.newPath)) {
					nextFileToReq = nextFileToReq.set(file.newPath, Set.empty(INT_ORDER));
				}
				if(file.commitState != CommitState.ADDED && !nextFileToReq.contains(file.oldPath)) {
					nextFileToReq = nextFileToReq.set(file.oldPath, Set.empty(INT_ORDER));
				}
				switch(file.commitState) {
				// move/insert/delete metadata based on commitState
				case ADDED:
					for(Integer req : reqs) {
						if(!nextReqToFile.contains(req)) {
							nextReqToFile = nextReqToFile.set(req, Set.single(FILE_ORDER, file.newPath));
						} else {
							nextReqToFile = nextReqToFile.set(req, nextReqToFile.get(req).some().insert(file.newPath));
						}
						nextFileToReq = nextFileToReq.set(file.newPath, nextFileToReq.get(file.newPath).some().insert(req));
					}
					break;
				case COPIED:
					nextFileToReq = nextFileToReq.set(file.newPath, nextFileToReq.get(file.oldPath).some());
					for(Integer req : nextFileToReq.get(file.oldPath).some()) {
						nextReqToFile = nextReqToFile.set(req, nextReqToFile.get(req).some().insert(file.newPath));
					}
					break;
				case DELETED:
					for(Integer req : nextFileToReq.get(file.oldPath).some()) {
						nextReqToFile = nextReqToFile.set(req, nextReqToFile.get(req).some().delete(file.oldPath));
					}
					nextFileToReq = nextFileToReq.delete(file.oldPath);
					break;
				case MODIFIED:
					for(Integer req : reqs) {
						if(!nextReqToFile.contains(req)) {
							nextReqToFile = nextReqToFile.set(req, Set.single(FILE_ORDER, file.newPath));
						} else {
							nextReqToFile = nextReqToFile.set(req, nextReqToFile.get(req).some().insert(file.newPath));
						}
						nextFileToReq = nextFileToReq.set(file.newPath, nextFileToReq.get(file.newPath).some().insert(req));
					}
					break;
				case RENAMED:
					nextFileToReq = nextFileToReq.set(file.newPath, nextFileToReq.get(file.oldPath).some());
					for(Integer req : nextFileToReq.get(file.oldPath).some()) {
						nextReqToFile = nextReqToFile.set(req, nextReqToFile.get(req).some().insert(file.newPath));
					}
					for(Integer req : nextFileToReq.get(file.oldPath).some()) {
						nextReqToFile = nextReqToFile.set(req, nextReqToFile.get(req).some().delete(file.oldPath));
					}
					nextFileToReq = nextFileToReq.delete(file.oldPath);
					for(Integer req : reqs) {
						if(!nextReqToFile.contains(req)) {
							nextReqToFile = nextReqToFile.set(req, Set.single(FILE_ORDER, file.newPath));
						} else {
							nextReqToFile = nextReqToFile.set(req, nextReqToFile.get(req).some().insert(file.newPath));
						}
						nextFileToReq = nextFileToReq.set(file.newPath, nextFileToReq.get(file.newPath).some().insert(req));
					}
					break;
				default:
					break;
				}
			}
			if(subtrees.length() > 1) {
				// this is a merge commit, insert the entries of this parent
				for(P2<Integer,Set<File>> entry : nextReqToFile) {
					Integer req = entry._1();
					if(!commitToReqToFiles.get(history.root()).contains(req)) {
						commitToReqToFiles.put(history.root(), commitToReqToFiles.get(history.root()).set(req, Set.<File>empty(FILE_ORDER)));
					}
					if(entry._2() != null) for(File file : entry._2()) {
						Set<File> oldSet = commitToReqToFiles.get(history.root()).get(req).some();
						commitToReqToFiles.put(history.root(), commitToReqToFiles.get(history.root()).set(req, oldSet.insert(file)));
					}
				}
				for(P2<File,Set<Integer>> entry : nextFileToReq) {
					File file = entry._1();
					if(!commitToFileToReqs.get(history.root()).contains(file)) {
						commitToFileToReqs.put(history.root(), commitToFileToReqs.get(history.root()).set(file, Set.<Integer>empty(INT_ORDER)));
					}
					for(Integer req : entry._2()) {
						Set<Integer> oldSet = commitToFileToReqs.get(history.root()).get(file).some();
						commitToFileToReqs.put(history.root(), commitToFileToReqs.get(history.root()).set(file, oldSet.insert(req)));
					}
				}
			} else {
				// this is a simple commit with one parent,
				// we can use the next maps we already merged
				commitToReqToFiles.put(history.root(), nextReqToFile);
				commitToFileToReqs.put(history.root(), nextFileToReq);
			}
		}
	}
}
