package com.telerikacademy.web.photocontest.services.contracts;

import com.telerikacademy.web.photocontest.models.Photo;
import com.telerikacademy.web.photocontest.models.PhotoReview;
import com.telerikacademy.web.photocontest.models.User;

import java.util.List;

public interface PhotoServices {
    List<Photo> getAll();
    void create(Photo photo);
    void delete(Photo photo, User user);

    Photo getById(Long id);

    void postReview(PhotoReview photoReview, Photo photo, User user);
}
