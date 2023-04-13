package com.telerikacademy.web.photocontest.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ResultEmbed implements Serializable {
    @ManyToOne
    @JoinColumn(name = "contest_id")
    private Contest contest;
    @ManyToOne
    @JoinColumn(name = "photo_id")
    private Photo photo;
}
