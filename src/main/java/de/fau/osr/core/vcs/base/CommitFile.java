package de.fau.osr.core.vcs.base;

import java.io.File;


/**
 * @author Gayathery
 * @desc This entity class holds the properties of a committed file.
 *
 */

public class CommitFile {

	public CommitState commitState;
	public File oldPath;
	public File newPath;
	
	
	/**
	 * @author Gayathery
	 */
	public CommitFile()
	{
		commitState = CommitState.NOTSET;
		oldPath = null;
		newPath = null;
	}
	
	/**
	 * @author Gayathery
	 * @param oldPath
	 * @param newPath
	 * @param commitState
	 */
	public CommitFile(File oldPath, File newPath, CommitState commitState)
	{
		this.commitState = commitState;
		this.oldPath = oldPath;
		this.newPath = newPath;
	}
}
