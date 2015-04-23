package de.fau.osr.core.vcs.base;

import java.io.File;
import java.util.Objects;


/**
 * @author Gayathery
 * @desc This entity class holds the properties of a committed file.
 *
 */

public class CommitFile {

	public CommitState commitState;
	public File oldPath;
	public File newPath;
	public String commitID;
	
	/**
	 * @author Gayathery
	 * @param oldPath
	 * @param newPath
	 * @param commitState
	 * @param commitID
	 */
	public CommitFile(File oldPath, File newPath, CommitState commitState, String commitID)
	{
		this.commitState = commitState;
		this.oldPath = oldPath;
		this.newPath = newPath;
		this.commitID = commitID;
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
