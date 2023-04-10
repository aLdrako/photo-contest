package com.telerikacademy.web.photocontest.services.contracts;

import com.telerikacademy.web.photocontest.models.User;

import java.util.List;
import java.util.Optional;

public interface UserServices {
    List<User> getAll();
    void update(User user, User userFromAuthorization);
    void create(User user);
    User getById(Long id);
    User getByUsername(String username);
    List<User> getAllOrganizers();
    List<User> getUsersWithJuryPermission();
    void delete(Long id, User userFromAuthorization);
    List<User> search(Optional<String> keyword);
}
