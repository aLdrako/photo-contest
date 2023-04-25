package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.exceptions.EntityDuplicateException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.models.Ranks;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.repositories.contracts.UserRepository;
import com.telerikacademy.web.photocontest.services.contracts.RankingServices;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.telerikacademy.web.photocontest.helpers.Helpers.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserServicesImplTests {
    @Mock
    UserRepository mockRepository;
    @Mock
    RankingServices mockRankingServices;
    @InjectMocks
    UserServicesImpl services;

    @Test
    public void getById_Should_CallRepository() {
        // Arrange
        User user = createMockUser();

        Mockito.when(mockRepository.getById(anyLong()))
                .thenReturn(user);

        // Act, Assert
        Assertions.assertEquals(user, services.getById(anyLong()));
    }
    @Test
    public void getById_Should_ThrowException_When_UserNotFound() {
        // Arrange
        Mockito.when(mockRepository.getById(anyLong()))
                .thenThrow(EntityNotFoundException.class);

        // Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> services.getById(anyLong()));
    }
    @Test
    public void getByUsername_Should_ThrowException_When_UserNotFound() {
        // Arrange
        Mockito.when(mockRepository.getByUsername(anyString()))
                .thenThrow(EntityNotFoundException.class);

        // Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> services.getByUsername(anyString()));
    }
    @Test
    public void getByUsername_Should_CallRepository() {
        // Arrange
        User user = createMockUser();

        Mockito.when(mockRepository.getByUsername(anyString()))
                .thenReturn(user);

        // Act, Assert
        Assertions.assertEquals(user, services.getByUsername(anyString()));
    }
    @Test
    public void getAll_Should_CallRepository() {
        // Arrange

        Mockito.when(mockRepository.findAllByIsDeletedFalse())
                .thenReturn(List.of());

        // Act, Assert
        Assertions.assertEquals(List.of(), services.getAll());
    }
    @Test
    public void getAllOrganizers_Should_CallRepository() {
        // Arrange

        Mockito.when(mockRepository.getAllOrganizers())
                .thenReturn(List.of());

        // Act, Assert
        Assertions.assertEquals(List.of(), services.getAllOrganizers());
    }
    @Test
    public void getUsersWithJuryPermission_Should_CallRepository() {
        // Arrange

        Mockito.when(mockRepository.getUsersWithJuryPermission())
                .thenReturn(List.of());

        // Act, Assert
        Assertions.assertEquals(List.of(), services.getUsersWithJuryPermission());
    }
    @Test
    public void search_Should_CallRepository() {
        // Arrange

        Mockito.when(mockRepository.search(""))
                .thenReturn(List.of());

        // Act, Assert
        Assertions.assertEquals(List.of(), services.search(Optional.empty()));
    }
    @Test
    public void update_Should_CallRepository_When_UserFromAuthenticationHasSameUsername() {
        // Arrange
        User user = createMockUser();
        User userFromAuthentication = createMockUser();

        Mockito.when(mockRepository.getByEmail(anyString()))
                .thenThrow(EntityNotFoundException.class);
        // Act
        services.update(user, userFromAuthentication);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .save(user);
    }
    @Test
    public void update_Should_CallRepository_When_UserFromAuthenticationIsOrganizer() {
        // Arrange
        User user = createMockUser();
        User userFromAuthentication = createMockOrganizer();
        userFromAuthentication.setUsername("anotherMockUsername");

        Mockito.when(mockRepository.getByEmail(anyString()))
                .thenReturn(user);
        // Act
        services.update(user, userFromAuthentication);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .save(user);
    }
    @Test
    public void update_Should_ThrowException_When_UserFromAuthenticationIsNotCreatorNorOrganizer() {
        // Arrange
        User user = createMockUser();
        User userFromAuthentication = createMockUser();
        userFromAuthentication.setUsername("anotherMockUsername");

        // Act, Assert
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> services.update(user, userFromAuthentication));
    }
    @Test
    public void update_Should_ThrowException_When_NewEmailAlreadyExist() {
        // Arrange
        User user = createMockUser();
        User userFromAuthentication = createMockUser();
        User userFromRepo = createMockUser();
        userFromRepo.setId(3L);

        Mockito.when(mockRepository.getByEmail(anyString()))
                .thenReturn(userFromRepo);

        // Act, Assert
        Assertions.assertThrows(EntityDuplicateException.class,
                () -> services.update(user, userFromAuthentication));
    }
    @Test
    public void create_Should_CallRepository_When_EmailAndUsernameAreUnique() {
        // Arrange
        User user = createMockUser();

        Mockito.when(mockRepository.getByEmail(anyString()))
                .thenThrow(EntityNotFoundException.class);

        Mockito.when(mockRepository.getByUsername(anyString()))
                .thenThrow(EntityNotFoundException.class);
        // Act
        services.create(user);
        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .save(user);
    }
    @Test
    public void create_Should_ThrowException_When_EmailIsDuplicate() {
        // Arrange
        User user = createMockUser();
        user.setId(null);

        Mockito.when(mockRepository.getByEmail(anyString()))
                .thenReturn(createMockUser());

        // Act, Assert
        Assertions.assertThrows(EntityDuplicateException.class,
                () -> services.create(user));
    }
    @Test
    public void create_Should_ThrowException_When_UsernameIsDuplicate() {
        // Arrange
        User user = createMockUser();
        user.setId(null);

        Mockito.when(mockRepository.getByEmail(anyString()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.getByUsername(anyString()))
                .thenReturn(createMockUser());

        // Act, Assert
        Assertions.assertThrows(EntityDuplicateException.class,
                () -> services.create(user));
    }
    @Test
    public void delete_Should_CallRepository() {
        // Arrange
        User user = createMockUser();

        Mockito.when(mockRepository.getById(anyLong()))
                .thenReturn(user);
        Mockito.when(mockRankingServices.getJunkie())
                .thenReturn(createMockRanking(Ranks.JUNKIE));
        // Act
        services.delete(user.getId(), user);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .save(user);
    }
    @Test
    public void delete_Should_ThrowException_When_UserNotFound() {
        // Arrange
        User user = createMockUser();

        Mockito.when(mockRepository.getById(anyLong()))
                .thenThrow(EntityNotFoundException.class);

        // Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> services.delete(user.getId(), user));
    }
    @Test
    public void delete_Should_ThrowException_When_UserFromAuthenticationIsNotCreatorNorOrganizer() {
        // Arrange
        User user = createMockUser();
        User user1 = createMockUser();
        user1.setUsername("anotherMockUsername");
        Mockito.when(mockRepository.getById(anyLong()))
                .thenReturn(user);

        // Act, Assert
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> services.delete(user.getId(), user1));
    }
    @Test
    public void delete_Should_CallRepository_When_UserIsOrganizer() {
        // Arrange
        User user = createMockUser();
        User user1 = createMockOrganizer();

        Mockito.when(mockRepository.getById(anyLong()))
                .thenReturn(user);
        // Act
        services.delete(user.getId(), user1);
        // Act, Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .save(user);
    }
}
