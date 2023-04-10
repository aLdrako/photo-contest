package com.telerikacademy.web.photocontest.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "photos_reviews")
public class PhotoReview {

    @EmbeddedId
    private ReviewId reviewId;

    @Column(name = "comment")
    private String comment;
    @Column(name = "score")
    private int score;
    @Column(name = "fits_category")
    private boolean fitsCategory;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhotoReview that = (PhotoReview) o;
        return Objects.equals(reviewId, that.reviewId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewId);
    }
}
