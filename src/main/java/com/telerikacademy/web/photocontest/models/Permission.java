package com.telerikacademy.web.photocontest.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "permissions")
public class Permission {
    @Id
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "is_organizer")
    private boolean isOrganiser;
    @Column(name = "is_deleted")
    private boolean isDeleted;

}
