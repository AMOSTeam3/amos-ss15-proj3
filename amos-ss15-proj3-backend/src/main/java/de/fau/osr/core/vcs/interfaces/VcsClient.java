package de.fau.osr.core.vcs.interfaces;

import de.fau.osr.core.db.DataSource;
import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.VcsEnvironment;
import de.fau.osr.core.vcs.impl.GitVcsClient;
import de.fau.osr.gui.Model.DataElements.AnnotatedLine;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Gayathery
 * 
 */
public abstract class VcsClient {

    public abstract Iterator<String> getBranchList();
    public abstract Iterator<String> getCommitList();
    public abstract String getRepositoryName();
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
    public abstract List<AnnotatedLine> blame(String path,
            DataSource dataSource) throws IOException, GitAPIException;

    
}
