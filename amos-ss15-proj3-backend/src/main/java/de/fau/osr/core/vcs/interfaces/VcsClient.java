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
     * Blame lines in the argument file on requirements.
     * This is potentially slow, so it should possibly be called asynchronously.
     *
     * @author Tobias
     * @return The annotated lines of the input file in the original order.
     * @throws IOException
     * @throws GitAPIException
     */
    public abstract List<AnnotatedLine> blame(String path,
            DataSource dataSource) throws IOException, GitAPIException;
    
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
