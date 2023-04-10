package com.telerikacademy.web.photocontest.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Embeddable
public class ReviewId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "photo_id")
    private Photo photoId;
    @ManyToOne
    @JoinColumn(name = "jury_id")
    private User juryId;

    public ReviewId(){}
    public ReviewId(Long photoId, Long juryId) {
        setPhotoId(photoId);
        setJuryId(juryId);
    }
    public void setPhotoId(Long photoId) {
        Photo photo = new Photo();
        photo.setId(photoId);
        this.photoId = photo;
    }
    public void setJuryId(Long juryId) {
        User user = new User();
        user.setId(juryId);
        this.juryId = user;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewId reviewId = (ReviewId) o;
        return Objects.equals(photoId, reviewId.photoId) && Objects.equals(juryId, reviewId.juryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(photoId, juryId);
    }
}
