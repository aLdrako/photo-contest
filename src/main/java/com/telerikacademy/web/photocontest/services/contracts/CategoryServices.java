package com.telerikacademy.web.photocontest.services.contracts;

import com.telerikacademy.web.photocontest.models.Category;

public interface CategoryServices {
    Iterable<Category> findAll();
    Category findById(Long id);
    Category save(Category category);
    void deleteById(Long id);
}
