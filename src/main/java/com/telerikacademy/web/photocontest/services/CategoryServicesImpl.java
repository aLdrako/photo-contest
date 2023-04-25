package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.exceptions.EntityDuplicateException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.models.Category;
import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.repositories.contracts.CategoryRepository;
import com.telerikacademy.web.photocontest.repositories.contracts.ContestRepository;
import com.telerikacademy.web.photocontest.services.contracts.CategoryServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServicesImpl implements CategoryServices {
    private static final String UNAUTHORIZED_MANIPULATION_MESSAGE = "Only users with Organizer role can create, update or delete categories!";
    private static final String DEFAULT_CATEGORY_CHANGE_ERROR_MESSAGE = "Default category cannot be changed or deleted!";

    private final CategoryRepository categoryRepository;
    private final ContestRepository contestRepository;

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
        if (category.getId() != null) checkDefaultCategoryPermissions(category.getId());
        checkUniqueness(category);
        return categoryRepository.save(category);
    }

    @Override
    public void deleteById(Long id, User authenticatedUser) {
        checkOrganizerPermissions(authenticatedUser);
        checkDefaultCategoryPermissions(id);
        Category category = findById(id);
        Category defaultCategory = findById(1L);
        List<Contest> allContests = contestRepository.findAll();
        allContests.stream().filter(contest -> contest.getCategory().equals(category))
                .forEach(contest -> {
                    contest.setCategory(defaultCategory);
                    contestRepository.save(contest);
                });
        categoryRepository.deleteById(category.getId());
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

    private void checkDefaultCategoryPermissions(Long id) {
        if (id == 1L) {
            throw new UnauthorizedOperationException(DEFAULT_CATEGORY_CHANGE_ERROR_MESSAGE);
        }
    }
}
