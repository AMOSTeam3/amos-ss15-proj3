package de.fau.osr.core.vcs.base;

import java.util.List;
import java.util.Set;

/**
 * This Class is a Container within the test framework for all information related to one commit.
 * @author: Florian Gerdes
 */
public class Commit {
    private Set<String> requirements;

    private String id;
    private String message;
    private List<CommitFile> commitFiles;
    public Commit(String id, String message, Set<String> requirements, List<CommitFile> commitFiles) {
        this.setId(id);
        this.setMessage(message);
        this.setRequirements(requirements);
        this.setCommitFiles(commitFiles);
    }

    public Set<String> getRequirements() {
        return requirements;
    }

    public void setRequirements(Set<String> requirements) {
        this.requirements = requirements;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CommitFile> getCommitFiles() {
        return commitFiles;
    }

    public void setCommitFiles(List<CommitFile> commitFiles) {
        this.commitFiles = commitFiles;
    }

}
