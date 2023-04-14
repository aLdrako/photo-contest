package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.exceptions.EntityDuplicateException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.models.Category;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.repositories.contracts.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.telerikacademy.web.photocontest.helpers.Helpers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServicesImplTests {

    @Mock
    CategoryRepository mockCategoryRepository;
    @InjectMocks
    CategoryServicesImpl categoryServices;

    @Test
    public void findAll_Should_CallRepository() {
        // Arrange
        when(mockCategoryRepository.findAll()).thenReturn(null);

        // Act
        categoryServices.findAll();

        // Assert
        verify(mockCategoryRepository).findAll();
    }

    @Test
    public void getById_Should_CallRepository() {
        // Arrange
        Category mockCategory = createMockCategory();

        when(mockCategoryRepository.findById(anyLong())).thenReturn(Optional.of(mockCategory));

        // Act
        categoryServices.findById(anyLong());

        // Assert
        verify(mockCategoryRepository).findById(anyLong());
    }

    @Test
    public void getById_Should_ThrowException_When_CategoryDoesNotExist() {
        // Arrange
        Category mockCategory = createMockCategory();

        when(mockCategoryRepository.findById(anyLong()))
                .thenThrow(EntityNotFoundException.class);

        // Act, Assert
        assertThrows(EntityNotFoundException.class,
                () -> categoryServices.findById(mockCategory.getId()));
    }

    @Test
    public void save_Should_CallRepository_When_CategoryWithSameNameDoesNotExist() {
        // Arrange
        Category mockCategory = createMockCategory();
        User mockOrganizer = createMockOrganizer();

        when(mockCategoryRepository.existsByNameEqualsIgnoreCase(anyString())).thenReturn(false);

        // Act
        categoryServices.save(mockCategory, mockOrganizer);

        // Assert
        verify(mockCategoryRepository).save(mockCategory);
    }

    @Test
    public void save_Should_ThrowException_When_UserIsNotOrganizer() {
        // Arrange
        Category mockCategory = createMockCategory();
        User mockUser = createMockUser();

        // Act, Assert
        assertThrows(UnauthorizedOperationException.class,
                () -> categoryServices.save(mockCategory, mockUser));
    }

    @Test
    public void save_Should_ThrowException_When_CategoryWithSameNameAlreadyExist() {
        // Arrange
        Category mockCategory = createMockCategory();
        User mockOrganizer = createMockOrganizer();

        when(mockCategoryRepository.existsByNameEqualsIgnoreCase(anyString())).thenReturn(true);

        // Act, Assert
        assertThrows(EntityDuplicateException.class,
                () -> categoryServices.save(mockCategory, mockOrganizer));
    }

    @Test
    public void deleteById_Should_CallRepository_WhenCategoryExists() {
        // Arrange
        Category mockCategory = createMockCategory();
        User mockOrganizer = createMockOrganizer();

        when(mockCategoryRepository.findById(mockCategory.getId())).thenReturn(Optional.of(mockCategory));

        // Act
        categoryServices.deleteById(mockCategory.getId(), mockOrganizer);

        // Assert
        verify(mockCategoryRepository).deleteById(mockCategory.getId());
    }

    @Test
    public void deleteById_Should_ThrowException_When_CategoryDoesNotExist() {
        // Arrange
        Category mockCategory = createMockCategory();
        User mockOrganizer = createMockOrganizer();

        when(mockCategoryRepository.findById(anyLong()))
                .thenThrow(EntityNotFoundException.class);

        // Act, Assert
        assertThrows(EntityNotFoundException.class,
                () -> categoryServices.deleteById(mockCategory.getId(), mockOrganizer));
    }

    @Test
    public void deleteById_Should_ThrowException_When_UserIsNotOrganizer() {
        // Arrange
        Category mockCategory = createMockCategory();
        User mockUser = createMockUser();

        // Act, Assert
        assertThrows(UnauthorizedOperationException.class,
                () -> categoryServices.deleteById(mockCategory.getId(), mockUser));
    }
}
