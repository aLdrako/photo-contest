package com.telerikacademy.web.photocontest.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Table(name = "contests_results")
public class ContestResults {

    @EmbeddedId
    private ResultEmbed resultEmbed;
    @Column(name = "results")
    private int results;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ContestResults that = (ContestResults) o;
        return getResultEmbed() != null && Objects.equals(getResultEmbed(), that.getResultEmbed());
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultEmbed);
    }
}
