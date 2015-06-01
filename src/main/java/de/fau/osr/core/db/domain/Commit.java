package de.fau.osr.core.db.domain;

import javax.persistence.*;
import java.util.HashSet;
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
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    private String id;

    @ManyToMany(mappedBy="commits", fetch = FetchType.EAGER)
    public Set<Requirement> getRequirements() {return reqs;}
    public void setRequirements(Set<Requirement> reqs) {this.reqs = reqs;}
    private Set<Requirement> reqs = new HashSet<>();

    public Commit() {}

    public Commit(String id) {
        this.id = id;
    }


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
