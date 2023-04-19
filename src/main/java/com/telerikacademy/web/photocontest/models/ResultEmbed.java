package com.telerikacademy.web.photocontest.models;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class ResultEmbed implements Serializable {
    @ManyToOne
    @JoinColumn(name = "contest_id")
    private Contest contest;
    @ManyToOne
    @JoinColumn(name = "photo_id")
    private Photo photo;

    public ResultEmbed() {
    }

    public ResultEmbed(Contest contest, Photo photo) {
        this.contest = contest;
        this.photo = photo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultEmbed that = (ResultEmbed) o;
        return Objects.equals(contest, that.contest) && Objects.equals(photo, that.photo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contest, photo);
    }
}
