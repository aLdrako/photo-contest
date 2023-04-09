package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.exceptions.EntityDuplicateException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.models.Category;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.repositories.contracts.CategoryRepository;
import com.telerikacademy.web.photocontest.services.contracts.CategoryServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryServicesImpl implements CategoryServices {
    private static final String UNAUTHORIZED_MANIPULATION_MESSAGE = "Only users with Organizer role can create, update or delete categories!";
    private final CategoryRepository categoryRepository;

    @Override
    public Iterable<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category", id));
    }

    @Override
    public Category save(Category category, User authenticatedUser) {
        checkOrganizerPermissions(authenticatedUser);
        checkUniqueness(category);
        return categoryRepository.save(category);
    }

    @Override
    public void deleteById(Long id, User authenticatedUser) {
        checkOrganizerPermissions(authenticatedUser);
        categoryRepository.deleteById(findById(id).getId());
    }

    private void checkUniqueness(Category category) {
        if (categoryRepository.existsByNameEqualsIgnoreCase(category.getName())) {
            throw new EntityDuplicateException("Category", "name", category.getName());
        }
    }

    private void checkOrganizerPermissions(User authenticatedUser) {
        if (!authenticatedUser.isOrganizer()) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_MANIPULATION_MESSAGE);
        }
    }
}
