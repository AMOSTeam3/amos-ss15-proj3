package de.fau.osr.core.vcs.interfaces;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;

import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.VcsEnvironment;
import de.fau.osr.core.vcs.impl.GitVcsClient;
import de.fau.osr.util.parser.CommitMessageParser;

/**
 * @author Gayathery
 * 
 */
public abstract class VcsClient {

	public abstract Iterator<String> getBranchList();
	public abstract Iterator<String> getCommitList();
	public abstract ArrayList<CommitFile> getCommitFiles(String commitID);
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
	 * Blame lines in the argument file on requirements.
	 * This is potentially slow, so it should possibly be called asynchronously.
	 * 
	 * @author Tobias
	 * @return The annotated lines of the input file in the original order.
	 * @throws IOException
	 * @throws GitAPIException
	 */
	public abstract Collection<AnnotatedLine> blame(String path,
			CommitMessageParser dataSource) throws IOException, GitAPIException;
	
	static public class AnnotatedLine {
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((line == null) ? 0 : line.hashCode());
			result = prime * result
					+ ((requirements == null) ? 0 : requirements.hashCode());
			return result;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			AnnotatedLine other = (AnnotatedLine) obj;
			if (line == null) {
				if (other.line != null)
					return false;
			} else if (!line.equals(other.line))
				return false;
			if (requirements == null) {
				if (other.requirements != null)
					return false;
			} else if (!requirements.equals(other.requirements))
				return false;
			return true;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "AnnotatedLine [requirements=" + requirements + ", line="
					+ line + "]";
		}
		public AnnotatedLine(Collection<String> requirements, String line) {
			this.requirements = requirements;
			this.line = line;
		}
		private Collection<String> requirements;
		/**
		 * @return all associated requirements
		 */
		public Iterable<String> getRequirements() {
			return requirements;
		}
		private String line;
		/**
		 * @return one line of code
		 */
		public String getLine() {
			return line;
		}
	}
}
