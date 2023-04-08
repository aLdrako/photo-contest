package com.telerikacademy.web.photocontest.services.contracts;

import com.telerikacademy.web.photocontest.models.User;

import java.util.List;

public interface UserServices {
    List<User> getAll();
    void update(User user, User userFromAuthorization);
    void create(User user);
    User getById(Long id);

    User getByUsername(String username);
    List<User> getAllOrganizers();
}
