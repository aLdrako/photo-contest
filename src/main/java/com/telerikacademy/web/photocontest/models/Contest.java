package com.telerikacademy.web.photocontest.models;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "contests")
@SecondaryTable(name = "cover_photos", pkJoinColumns = @PrimaryKeyJoinColumn(name = "contest_id"))
public class Contest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    private String title;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "is_invitational")
    private boolean isInvitational;
    @Column(name = "phase1")
    private LocalDateTime phase1;
    @Column(name = "phase2")
    private LocalDateTime phase2;
    @Generated(GenerationTime.ALWAYS)
    @Column(name = "date_created")
    private LocalDateTime dateCreated;
    @Lob
    @Column(name = "cover_photo", table = "cover_photos")
    private String coverPhoto;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "juries",
            joinColumns = @JoinColumn(name = "contest_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> juries;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "participants",
            joinColumns = @JoinColumn(name = "contest_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> participants;
    @OneToMany(mappedBy = "postedOn", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<Photo> photos;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Contest contest = (Contest) o;
        return getId() != null && Objects.equals(getId(), contest.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
