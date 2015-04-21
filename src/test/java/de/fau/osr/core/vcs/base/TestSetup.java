package de.fau.osr.core.vcs.base;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;

import de.fau.osr.PrivateTestData;

public class TestSetup {
	private static final String TEST_REPOSITORY_URI = "https://github.com/uv48uson/Test_Repository.git";
	private enum RepoState{Update, Create};
	private static RepoState repoState = RepoState.Update;
	
	public static void main(String[] args){
		switch(repoState){
			case Update:
				updateRepo();
				break;
			case Create:
				createRepo();
		}
		
		
	}
	
	private static void createRepo() {
		File localPath = new File(PrivateTestData.getGitTestRepo());
		try {
			Git clone = Git.cloneRepository().setURI(TEST_REPOSITORY_URI).setDirectory(localPath).call();
			clone.getRepository().close();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void updateRepo(){
		Repository localRepo = null;
		
		try {
			localRepo = new FileRepository(PrivateTestData.getGitTestRepo());
		} catch (IOException e) {
			e.printStackTrace();  
		}
		
		Git git = new Git(localRepo);

		PullCommand pullCmd = git.pull();
		try {
			pullCmd.call();
		} catch (GitAPIException e) {
			e.printStackTrace();  
		}
	}
}
