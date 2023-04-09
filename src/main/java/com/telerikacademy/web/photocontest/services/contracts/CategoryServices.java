package com.telerikacademy.web.photocontest.services.contracts;

import com.telerikacademy.web.photocontest.models.Category;
import com.telerikacademy.web.photocontest.models.User;

public interface CategoryServices {
    Iterable<Category> findAll();
    Category findById(Long id);
    Category save(Category category, User authenticatedUser);
    void deleteById(Long id, User authenticatedUser);
}
