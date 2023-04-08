package com.telerikacademy.web.photocontest.repositories.contracts;

import com.telerikacademy.web.photocontest.models.User;

import java.util.List;

public interface UserRepository {
    List<User> getAll();
    User getById(Long id);
    void update(User user);
    void create(User user);
    void delete(User user);
    User getByEmail(String email);
    User getByUsername(String username);
    List<User> getAllOrganizers();
}
