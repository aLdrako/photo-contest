package com.telerikacademy.web.photocontest.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "photos")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "story")
    private String story;
    @Column(name = "photo")
    private String photo;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userCreated;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contest_id")
    private Contest postedOn;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reviewId.photoId")
    private Set<PhotoScore> scores = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reviewId.photoId")
    private Set<PhotoReviewDetails> reviewsDetails = new HashSet<>();

    public void addScore(PhotoScore photoScore) {
        scores.add(photoScore);
    }
    public void addReviewDetails(PhotoReviewDetails photoReviewDetails) {
        reviewsDetails.add(photoReviewDetails);
    }
    public void updateScore(PhotoScore photoScore) {
        scores.remove(photoScore);
        scores.add(photoScore);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo = (Photo) o;
        return Objects.equals(id, photo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
