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

import de.fau.osr.core.vcs.base.CommitFile;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * commit entity
 * Created by Dmitry Gorelenkov on 01.06.2015.
 */

@Entity
@Table(name="commits")
public class Commit {

    @Id
    @Column(name="id", nullable = false)
    private String id;

    @ManyToMany(mappedBy="commits", fetch = FetchType.EAGER)
    private Set<Requirement> reqs = new HashSet<>();

    @Column(name="message", nullable = true)
    private String message;

    @Column(name="author", nullable = true)
    private String author;

    @Transient //not saved yet, todo
    private List<CommitFile> commitFiles;

    public Commit() {}

    public Commit(String id) {
        this.id = id;
    }

    public Commit(String id, String message, String author, List<CommitFile> commitFiles, Set<Requirement> reqs) {
        this.id = id;
        this.reqs = reqs;
        this.message = message;
        this.author = author;
        this.commitFiles = commitFiles;
    }


    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public Set<Requirement> getRequirements() {return reqs;}
    public void setRequirements(Set<Requirement> reqs) {this.reqs = reqs;}

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }


    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    //TODO fill the commit files by tracker
    public List<CommitFile> getCommitFiles() { return commitFiles; }
    public void setCommitFiles(List<CommitFile> commitFiles) { this.commitFiles = commitFiles; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Commit)) return false;

        Commit commit = (Commit) o;

        return id.equals(commit.id);

    }


    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
