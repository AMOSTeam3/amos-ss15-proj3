package connection;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

/**
 * @author Florian Gerdes
 */
public class GitConnector implements Connector {

	@Override
	/**
	 * @author Florian Gerdes
	 * @return String containing the message of the latest Commit.
	 * if the project doesn't contain any messages, an IOException is thrown 
	 */
	public String getLatestCommitMessage(String repositoryUrl) throws Exception{
		Iterator<RevCommit> iterator = getCommitIteratorOfRepository(repositoryUrl);
	    
	    //getting the latest commit message
		if(!iterator.hasNext()){
			throw new IOException("No Commit found!");
		}
	    RevCommit commit = iterator.next();
	    return commit.getFullMessage();
	}

	@Override
	/**
	 * @author Florian Gerdes
	 * @return String containing the "name" of the ObjectId of the latest Commit.
	 * if the project doesn't contain any messages, an IOException is thrown 
	 */
	public String getLatestCommitId(String repositoryUrl) throws Exception{
		Iterator<RevCommit> iterator = getCommitIteratorOfRepository(repositoryUrl);
	    
	    //getting the latest commit message
		if(!iterator.hasNext()){
			throw new IOException("No Commit found!");
		}
	    RevCommit commit = iterator.next();
	    return commit.getId().getName();
	}
	
	/**
	 * @author Florian Gerdes
	 * @return Iterator<RevCommit> Commit Iterator
	 * if there is no proper connection to the git project (whether the specified project doesn't exist or
	 * the options are not compatible) the methode throws an IOException or an IllegalArgumentException.
	 * If there occures any internal JGit error a NoHead or GitAPIException is thrown.
	 * For more information notice FileRepositoryBuilder.build and LogCommend.call API. 
	 */
	private Iterator<RevCommit> getCommitIteratorOfRepository(String repositoryUrl) throws Exception{
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
	    Repository repo = builder.setGitDir(new File(repositoryUrl)).setMustExist(true).build();
		Git git = new Git(repo);
		Iterable<RevCommit> log = git.log().call();
	    Iterator<RevCommit> iterator = log.iterator();
	    return iterator;
	}

}
