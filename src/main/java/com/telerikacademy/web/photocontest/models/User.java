package com.telerikacademy.web.photocontest.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")
@SecondaryTables({
        @SecondaryTable(name = "points", pkJoinColumns = @PrimaryKeyJoinColumn(name = "user_id"))
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "join_date")
    private LocalDateTime joinDate;
    @ManyToOne
    @JoinColumn(name = "ranking_id")
    private Ranking rank;
    @Column(name = "points", table = "points")
    private int points;
    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "user_id")
    private Permission permission;
    @OneToMany(mappedBy = "userCreated")
    private Set<Photo> photos;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
