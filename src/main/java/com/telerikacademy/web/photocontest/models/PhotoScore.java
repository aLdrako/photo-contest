package com.telerikacademy.web.photocontest.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "photos_scores")
public class PhotoScore {

    @EmbeddedId
    private ReviewId reviewId;
    @Column(name = "score")
    private int score;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhotoScore that = (PhotoScore) o;
        return Objects.equals(reviewId, that.reviewId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewId);
    }
}
