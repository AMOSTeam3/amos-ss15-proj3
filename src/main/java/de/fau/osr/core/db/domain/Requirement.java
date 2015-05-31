package de.fau.osr.core.db.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Gayathery
 * Requirement entity
 */

@Entity
@Table(name="requirement")
public class Requirement {

    @Id
    @Column(name="id")
    private String id;  

    @Column(name="title")
    private String title;

    @Column(name="description")
    private String description;

    @Column(name="story_point")
    private int storyPoint;

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

        if (storyPoint != that.storyPoint) return false;
        if (!id.equals(that.id)) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        return !(description != null ? !description.equals(that.description) : that.description != null);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + storyPoint;
        return result;
    }


}
