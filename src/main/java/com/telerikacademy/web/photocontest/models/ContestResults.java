package com.telerikacademy.web.photocontest.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "contests_results")
public class ContestResults {

    @EmbeddedId
    private ResultEmbed resultEmbed;
    @Column(name = "results")
    private int results;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContestResults that = (ContestResults) o;
        return Objects.equals(resultEmbed, that.resultEmbed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultEmbed);
    }
}
