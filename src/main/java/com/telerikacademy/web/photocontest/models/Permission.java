package com.telerikacademy.web.photocontest.models;

import javax.persistence.*;

@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "is_organizer")
    private boolean isOrganiser;
    @Column(name = "is_deleted")
    private boolean isDeleted;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isOrganiser() {
        return isOrganiser;
    }

    public void setOrganiser(boolean organiser) {
        isOrganiser = organiser;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
