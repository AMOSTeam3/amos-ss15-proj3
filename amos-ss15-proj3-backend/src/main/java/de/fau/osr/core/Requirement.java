package de.fau.osr.core;

import java.util.HashSet;
import java.util.Set;

/**
 * Data Object, to keep requirement information
 * Created by Dmitry Gorelenkov on 22.06.2015.
 */
public class Requirement {
    private String id;

    private String title;

    private String description;

    private int storyPoint;

    private Set<String> commitIds = new HashSet<>();

    /**
     * Creates requirement object. In case any parameter except of id, is null, the object
     * will be initialized with default empty values
     *
     * @param id id of requirement, must be present and not empty
     */
    public Requirement(String id, String description, String title, Set<String> commitIds, int storyPoint) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID must be present");
        }

        this.id = id;
        this.description = (description != null) ? description : "";
        this.title = (title != null) ? title : "";
        this.commitIds = (commitIds != null) ? commitIds : new HashSet<>();
        this.storyPoint = storyPoint; //make sure story points are positive?
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getCommitIds() {
        return commitIds;
    }

    public void setCommitIds(Set<String> commitIds) {
        this.commitIds = commitIds;
    }

    public int getStoryPoint() {
        return storyPoint;
    }

    public void setStoryPoint(int storyPoint) {
        this.storyPoint = storyPoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Requirement)) return false;

        Requirement that = (Requirement) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
