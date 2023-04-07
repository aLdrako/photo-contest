package com.telerikacademy.web.photocontest.models;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "rankings")
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private Ranks name;

    @OneToMany
    private Set<User> users;

    public Ranking() {
    }

    public Ranking(Ranks name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Ranks getName() {
        return name;
    }

    public void setName(Ranks name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ranking ranking = (Ranking) o;
        return id == ranking.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
