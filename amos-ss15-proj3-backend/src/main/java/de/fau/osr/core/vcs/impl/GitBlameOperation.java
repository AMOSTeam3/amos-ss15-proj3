package de.fau.osr.core.vcs.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.regex.Matcher;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffAlgorithm;
import org.eclipse.jgit.diff.DiffAlgorithm.SupportedAlgorithm;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

import com.google.common.collect.Iterators;

import de.fau.osr.core.vcs.AnnotatedWords;
import de.fau.osr.core.vcs.interfaces.VcsClient;
import de.fau.osr.util.FlatSource;

/**
 * Similar to a git blame, this operation traverses history from the
 * current HEAD and looks for modifications of the file at the given path.
 * It annotates each word with its source commit(s) (plural in case of
 * ambiguousness), and all Objects returned by the given putBlame.
 * In contrast with git blame it traverses not only one, but all possible paths
 * through history, otherwise it would not be possible to permanently attach
 * annotations to lines in commits.
 * All annotations are either {@link ObjectId}s of commit objects or
 * Objects returned by putBlame.
 * @author tobias
 */
public class GitBlameOperation {

	/**
	 * @param client
	 * @param path relative to the working copy
	 * @param putBlame Given a Commit id and a line number return an Annotation or null.
	 * @throws ClassCastException if client is not a git client
	 */
	public GitBlameOperation(
			VcsClient client,
			String path,
			BiFunction<String,Integer,? extends Iterator<? extends Object>> putBlame) {
		this.client = (GitVcsClient) client;
		this.repo = this.client.repo;
		// convert path to unix style as expected by git
		this.path = path.replaceAll(Matcher.quoteReplacement("\\"), "/");
		this.putBlame = putBlame;
	}

	private final GitVcsClient client;
	private final Repository repo;
	private final String path;
	private final BiFunction<String,Integer,? extends Iterator<? extends Object>> putBlame;
	private final DiffAlgorithm diffAlgorithm = DiffAlgorithm.getAlgorithm(SupportedAlgorithm.HISTOGRAM);

	/**
	 * @author tobias
	 * This class serves as a todo item in the wordBlame process.
	 * It has a natural descending ordering over the commit timestamps,
	 * while it compares without regard to the currently active words
	 * (equal instances are later merged).
	 */
	private static class BlameItem implements Comparable<BlameItem>, Cloneable {
		/**
		 * @param accused
		 * @param chunks
		 */
		public BlameItem(RevCommit accused, TreeMap<Integer,Integer> words, String path) {
			this.accused = accused;
			this.words = words;
			this.path = path;
		}
		RevCommit accused;
		
		/*
		 * words maps all suspect word indices from the current index to
		 * the one at the root of the blame
		 */
		TreeMap<Integer,Integer> words;
		
		/*
		 * this path might be different from the path at the beginning after
		 * rename detection
		 */
		String path;
		@Override
		public int compareTo(BlameItem other) {
			int dateCmp = other.accused.getAuthorIdent().getWhen().compareTo(accused.getAuthorIdent().getWhen());
			if(dateCmp != 0) return dateCmp;
			int accCmp = accused.compareTo(other.accused);
			if(accCmp != 0) return accCmp;
			return path.compareTo(other.path);
		}
		@Override
		public boolean equals(Object o) {
			return (o instanceof BlameItem) &&
					accused.equals(((BlameItem)o).accused) &&
					path.equals(((BlameItem)o).path);
		}
		
		/**
		 * This method copies the fields, but afterwards, both "words" members
		 * refer to the same object. Be careful!
		 * @return a shallow copy of this object
		 */
		public BlameItem shallowClone() {
			BlameItem res;
			try {
				res = (BlameItem)super.clone();
			} catch (CloneNotSupportedException e) {
				// should never occur, panic if it does.
				throw new Error(e);
			}
			return res;
		}
		
	}

	private TreeSet<BlameItem> workQueue = new TreeSet<>();

	/**
	 * Push b into workQueue, merge if the commit already
	 * exists in the queue.
	 * @param b
	 */
	private void push(BlameItem b) {
		if(!workQueue.add(b)) {
			BlameItem old = workQueue.ceiling(b);
			workQueue.remove(b);
			old.words.putAll(b.words);
			workQueue.add(old);
		}
	}

	/**
	 * @throws GitAPIException
	 * @throws IOException
	 * @return An instance of {@link AnnotatedWords} where every word from
	 * the committed contents of path is mapped to a deduplicated list of annotations.
	 */
	public AnnotatedWords wordBlame() throws GitAPIException, IOException {


		/*
		 * at the moment, just look at the state at HEAD,
		 * could be expanded in the future to
		 * 	a) parameterize the used commit
		 * 	b) annotate the working copy instead 
		 */
		ObjectId rootId = repo.resolve("HEAD");
		RevWalk walker = new RevWalk(repo);
		RevCommit rootCommit = walker.parseCommit(rootId);
		byte[] content;
		try {
			content = client.readBlob(client.fileAtRev(walker, path, rootCommit));
		} catch (NullPointerException e1) {
			throw new FileNotFoundException(path);
		}

		FlatSource source = FlatSource.flatten(content);
		@SuppressWarnings("unchecked")
		List<Object>[] currentBlame = new List[source.size()];
		BlameItem topBlame = new BlameItem(rootCommit, new TreeMap<>(), path);

		/*
		 * initially, blame all lines on HEAD
		 */
		for(int i=0; i<currentBlame.length; ++i) {
			currentBlame[i] = new ArrayList<>();
			topBlame.words.put(i, i);
		}

		workQueue.add(topBlame);

		while(!workQueue.isEmpty()) {
			BlameItem cur = workQueue.pollFirst();
			walker.parseCommit(cur.accused);

			ObjectId idAfter = client.fileAtRev(walker, cur.path, cur.accused);
			FlatSource after = FlatSource.flatten(client.readBlob(idAfter));
			
			/*
			 * pull in custom annotations from putBlame on all suspect lines
			 */
			if(putBlame != null) {
				String nameOfAccused = cur.accused.name();
				for(Map.Entry<Integer,Integer> entry : cur.words.entrySet()) {
					Iterator<? extends Object> iterator = putBlame.apply(nameOfAccused, after.getLineByWord(entry.getKey()));
					if(iterator != null)
						Iterators.addAll(currentBlame[entry.getValue()], iterator);
				}
			}
			
			RevCommit[] parents = cur.accused.getParents();

			/*
			 * found indicates if we found an unmodified copy in a parent,
			 * if false, foundLines indicates which lines we were able to blame
			 * down in history
			 */
			boolean found = false;
			HashSet<Integer> foundLines = new HashSet<>();
			for(RevCommit parent : parents) {
				walker.parseCommit(parent);

				TreeWalk treeWalk = TreeWalk.forPath(repo, cur.path, cur.accused.getTree(), parent.getTree());
				if(treeWalk.idEqual(0, 1)) {
					//the file has not changed between parent and accused
					
					BlameItem nextItem = cur.shallowClone();
					nextItem.accused = parent;
					push(nextItem);
					found = true;
				} else {
					//the file has changed
					
					ObjectId idBefore = client.fileAtRev(walker, cur.path, parent);
					if(idBefore == null) {
						/*
						 * the file does not exist at the same path in parent and accused,
						 * so go look for identical files
						 * 
						 * could be extended to look for similar files, but watch performance!
						 */
						treeWalk = new TreeWalk(repo);
						treeWalk.setRecursive(true);
						treeWalk.addTree(parent.getTree());
						while(treeWalk.next()) {
							if(treeWalk.getObjectId(0).equals(idAfter)) {
								String pathBefore = treeWalk.getPathString();
								BlameItem nextItem = cur.shallowClone();
								nextItem.accused = parent;
								nextItem.path = pathBefore;
								push(nextItem);
								found = true;
								break;
							}
						}
						continue;
					}
					//the file is at the same location in parent
					
					byte[] byteBefore = client.readBlob(idBefore);
					EditList diff;
					FlatSource before = FlatSource.flatten(byteBefore);
					diff = diffAlgorithm.diff(RawTextComparator.DEFAULT, before, after);

					/*
					 * The documentation does not state if diff is sorted,
					 * just that the Edits do not overlap, but it is much
					 * more convenient to have it sorted.
					 */
					final Comparator<Edit> compEdit = (d1, d2) -> d1.getBeginB() - d2.getBeginB();
					diff.sort(compEdit);
					
					BlameItem passedBlame = new BlameItem(parent, new TreeMap<>(), cur.path);
					
					Iterator<Map.Entry<Integer, Integer>> entryIterator = cur.words.entrySet().iterator();
					Iterator<Edit> editIterator = diff.iterator();
					Map.Entry<Integer, Integer> curEntry = null;
					if(entryIterator.hasNext())
						curEntry = entryIterator.next();
					Edit curEdit = null;
					if(editIterator.hasNext())
						curEdit = editIterator.next();
					int offset = 0;
					
					/*
					 * traverse diff and words simultaneously
					 */
					while (curEntry != null || entryIterator.hasNext()) {
						if (curEntry == null) {
							curEntry = entryIterator.next();
						} else if (curEdit == null && editIterator.hasNext()) {
							curEdit = editIterator.next();
						} else if (curEdit != null && curEdit.getBeginB() <= curEntry.getKey()) {
							if(curEdit.getEndB() > curEntry.getKey()) {
								/*
								 * curEntry was erased by curEdit
								 */
								curEntry = null;
							} else {
								/*
								 * curEdit introduced an offset before curEntry
								 */
								offset += (curEdit.getEndA() - curEdit.getBeginA()) -
										(curEdit.getEndB() - curEdit.getBeginB());
								curEdit = null;
							}
						} else {
							/*
							 * push curEntry with key corrected by offset
							 */
							foundLines.add(curEntry.getKey());
							passedBlame.words.put(curEntry.getKey() + offset, curEntry.getValue());
							curEntry = null;
						}
					}
					/*
					 * push the lines we found in parent back to queue
					 */
					push(passedBlame);
				}
			}
			/*
			 * If there is not identical parent file, we have to take
			 * responsibility for all lines not found in some parent.
			 */
			if(!found){
				for(Map.Entry<Integer,Integer> entry : cur.words.entrySet()) {
					if(!foundLines.contains(entry.getKey()))
						currentBlame[entry.getValue()].add(cur.accused.getId());
				}
			}


		}

		/*
		 * duplicate objects take up unneeded space, clean them up 
		 */
		for(int i=0; i<currentBlame.length; ++i)
			currentBlame[i] = new ArrayList<>(new HashSet<>(currentBlame[i]));
		
		return new AnnotatedWords(source, currentBlame);
	}
}
