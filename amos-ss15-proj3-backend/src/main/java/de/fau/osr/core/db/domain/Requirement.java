/*
 * This file is part of ReqTracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * ReqTracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ReqTracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with ReqTracker.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fau.osr.core.db.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Gayathery
 * Requirement entity
 */

@Entity
@Table(name="requirement")
public class Requirement {

    @Id
    @Column(name="id", nullable = false)
    private String id;

    @Column(name="title")
    private String title;

    @Column(name="description")
    private String description;

    @Column(name="story_point")
    private Integer storyPoint;


    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(name="requirements_commits",
            joinColumns={@JoinColumn(name="req_id")},
            inverseJoinColumns={@JoinColumn(name="commit_id")})
    private Set<Commit> commits = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(name="requirements_commits_filtered",
            joinColumns={@JoinColumn(name="req_id")},
            inverseJoinColumns={@JoinColumn(name="commit_id")})
    private Set<Commit> filteredCommits = new HashSet<>();

    public Set<Commit> getFilteredCommits() { return filteredCommits; }

    public void setFilteredCommits(Set<Commit> filteredCommits) { this.filteredCommits = filteredCommits; }

    public Set<Commit> getCommits() {return commits;}
    public void setCommits(Set<Commit> commits) {this.commits = commits;}
    

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

    public Integer getStoryPoint() {
        return (storyPoint != null) ? storyPoint : -1;
    }

    public void setStoryPoint(Integer storyPoint) {
        this.storyPoint = storyPoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Requirement that = (Requirement) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
