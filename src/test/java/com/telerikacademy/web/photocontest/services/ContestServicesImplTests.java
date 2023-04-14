package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.repositories.contracts.ContestRepository;
import com.telerikacademy.web.photocontest.services.contracts.UserServices;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.telerikacademy.web.photocontest.helpers.Helpers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContestServicesImplTests {

    @Mock
    ContestRepository mockContestRepository;
    @Mock
    UserServices mockUserServices;
    @InjectMocks
    ContestServicesImpl contestServices;

    @Test
    public void findAll_Should_CallRepository() {
        // Arrange
        when(mockContestRepository.findAll()).thenReturn(null);

        // Act
        contestServices.findAll();

        // Assert
        verify(mockContestRepository).findAll();
    }

    @Test
    public void findById_Should_CallRepository() {
        // Arrange
        Contest mockContest = createMockContest();

        when(mockContestRepository.findById(anyLong())).thenReturn(Optional.of(mockContest));

        // Act
        contestServices.findById(anyLong());

        // Assert
        verify(mockContestRepository).findById(anyLong());
    }

    @Test
    public void findById_Should_ThrowException_When_ContestDoesNotExist() {
        // Arrange
        Contest mockContest = createMockContest();

        when(mockContestRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act, Assert
        assertThrows(EntityNotFoundException.class,
                () -> contestServices.findById(mockContest.getId()));
    }

    @Test
    public void update_Should_CallRepository_When_UserIsOrganizer() {
        // Arrange
        Contest mockContest = createMockContest();
        User mockOrganizer = createMockOrganizer();

        // Act
        contestServices.update(mockContest, mockOrganizer);

        // Assert
        verify(mockContestRepository).save(mockContest);
    }

    @Test
    public void update_Should_ThrowException_When_UserIsNotOrganizer() {
        // Arrange
        Contest mockContest = createMockContest();
        User mockUser = createMockUser();

        // Act, Assert
        assertThrows(UnauthorizedOperationException.class,
                () -> contestServices.update(mockContest, mockUser));
    }

    @Test
    public void uploadCover_Should_CallRepository_When_UserIsOrganizer() {
        // Arrange
        Contest mockContest = createMockContest();
        User mockOrganizer = createMockOrganizer();

        // Act
        contestServices.uploadCover(mockContest, mockOrganizer);

        // Assert
        verify(mockContestRepository).save(mockContest);
    }

    @Test
    public void uploadCover_Should_ThrowException_When_UserIsNotOrganizer() {
        // Arrange
        Contest mockContest = createMockContest();
        User mockUser = createMockUser();

        // Act, Assert
        assertThrows(UnauthorizedOperationException.class,
                () -> contestServices.uploadCover(mockContest, mockUser));
    }

    @Test
    public void deleteById_Should_CallRepository_When_ContestExists() {
        // Arrange
        Contest mockContest = createMockContest();
        User mockOrganizer = createMockOrganizer();

        when(mockContestRepository.findById(mockContest.getId())).thenReturn(Optional.of(mockContest));

        // Act
        contestServices.deleteById(mockContest.getId(), mockOrganizer);

        // Assert
        verify(mockContestRepository).deleteById(mockContest.getId());
    }

    @Test
    public void deleteById_Should_ThrowException_When_ContestDoesNotExist() {
        // Arrange
        Contest mockContest = createMockContest();
        User mockOrganizer = createMockOrganizer();

        when(mockContestRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act, Assert
        assertThrows(EntityNotFoundException.class,
                () -> contestServices.deleteById(mockContest.getId(), mockOrganizer));
    }

    @Test
    public void deleteById_Should_ThrowException_When_UserIsNotOrganizer() {
        // Arrange
        Contest mockContest = createMockContest();
        User mockUser = createMockUser();

        // Act, Assert
        assertThrows(UnauthorizedOperationException.class,
                () -> contestServices.deleteById(mockContest.getId(), mockUser));
    }

    @Test
    public void filter_Should_CallRepository() {
        // Arrange, Act
        contestServices.filter(anyString(), anyString(), anyBoolean(), anyBoolean(), any(), any());

        // Assert
        verify(mockContestRepository).filter(anyString(), anyString(), anyBoolean(), anyBoolean(), any(), any());
    }
}
