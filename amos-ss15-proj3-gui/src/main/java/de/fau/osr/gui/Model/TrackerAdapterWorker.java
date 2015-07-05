package de.fau.osr.gui.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.jgit.api.errors.GitAPIException;

import de.fau.osr.gui.Model.DataElements.Commit;
import de.fau.osr.gui.Model.DataElements.CommitFile;
import de.fau.osr.gui.Model.DataElements.Requirement;

/**
 * This class 'TrackerAdapterWorker' works as an indexer for the application.
 * This class collects data in the background and puts them into cached when
 * ready. It polls continually if the underlying version control data changed
 * and if stale, clears its cache. 
 * @author Gayathery
 */
public class TrackerAdapterWorker implements Runnable {

    public TrackerAdapterWorker(TrackerAdapter trackerAdapter) {
		this.trackerAdapter = trackerAdapter;
	}

	private TrackerAdapter trackerAdapter;
    public AtomicReference<CachedData> cached = new AtomicReference<>();
    
    public void setActualTrackerAdapter(TrackerAdapter trackerAdapter){
        this.trackerAdapter = trackerAdapter;
    }
    
    static class CachedData{
    	public CachedData(String head, Collection<CommitFile> totalCommitFiles,
				Collection<Requirement> requirements,
				HashMap<Requirement, Collection<Commit>> reqCommit,
				HashMap<Commit, Collection<CommitFile>> commitCommitFile,
				HashMap<Requirement, Collection<CommitFile>> reqCommitFile) {
			this.head = head;
			this.totalCommitFiles = totalCommitFiles;
			this.requirements = requirements;
			this.reqCommit = reqCommit;
			this.commitCommitFile = commitCommitFile;
			this.reqCommitFile = reqCommitFile;
		}
		final public String head;
    	final public Collection<CommitFile> totalCommitFiles;
    	final public Collection<Requirement> requirements;
    	final public HashMap<Requirement,Collection<Commit>> reqCommit;
    	final public HashMap<Commit,Collection<CommitFile>> commitCommitFile;
    	final public HashMap<Requirement,Collection<CommitFile>> reqCommitFile;
    }

    public void run() {
    	while(true) {
    		String head = "";
    		try {
    			head = trackerAdapter.getHeadId();
    			Collection<CommitFile> totalCommitFiles = new ArrayList<>();
    			Collection<Requirement> requirements = new ArrayList<>();
    			HashMap<Requirement,Collection<Commit>> reqCommit = new HashMap<>();
    			HashMap<Commit,Collection<CommitFile>> commitCommitFile = new HashMap<>();
    			HashMap<Requirement,Collection<CommitFile>> reqCommitFile = new HashMap<>();
    			requirements = trackerAdapter.getAllRequirements();
    			for(Requirement requirement : requirements){
    				Collection<Commit> commits = trackerAdapter.getCommitsFromRequirement(requirement);
    				reqCommitFile.put(requirement, trackerAdapter.getCommitFilesForRequirement(requirement));
    				reqCommit.put(requirement,commits);
    				for(Commit commit : commits){
    					Collection<CommitFile> commitFiles = trackerAdapter.getFilesFromCommit(commit);
    					commitCommitFile.put(commit,commitFiles);
    					totalCommitFiles.addAll(commitFiles);
    				}
    			}
    			cached.set(new CachedData(head, totalCommitFiles, requirements, reqCommit, commitCommitFile, reqCommitFile));
    		} catch (IOException | GitAPIException e) {
    			// something went wrong, try again in 10 seconds
    			cached.set(null);
    			try {
    				Thread.sleep(10*1000);
    			} catch (InterruptedException ei) {
    				// something went really wrong, bail out
    				throw new RuntimeException(ei);
    			}
    		}
    		while(cached.get() != null) {
    			//sleep 5 seconds waiting for changes at the vcs
    			try {
					Thread.sleep(5*1000);
					if(!trackerAdapter.getHeadId().equals(head))
						cached.set(null);
				} catch (InterruptedException | GitAPIException | IOException e) {
					cached.set(null);
				}
    		}
    	}
    }
}
