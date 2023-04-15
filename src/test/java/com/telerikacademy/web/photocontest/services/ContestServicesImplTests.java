package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.exceptions.EntityDuplicateException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.models.Ranking;
import com.telerikacademy.web.photocontest.models.Ranks;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.repositories.contracts.ContestRepository;
import com.telerikacademy.web.photocontest.services.contracts.RankingServices;
import com.telerikacademy.web.photocontest.services.contracts.UserServices;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.*;

import static com.telerikacademy.web.photocontest.helpers.Helpers.*;
import static com.telerikacademy.web.photocontest.helpers.Helpers.createMockRanking;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContestServicesImplTests {

    @Mock
    ContestRepository mockContestRepository;
    @Mock
    UserServices mockUserServices;
    @Mock
    RankingServices mockRankingService;
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

    @Test
    public void create_Should_CallRepository_WithoutAddedJuries() {
        // Arrange
        Contest mockContest = createMockContestDynamic();
        User mockOrganizer = createMockOrganizer();
        mockContest.setJuries(null);

        when(mockUserServices.getAllOrganizers()).thenReturn(new ArrayList<>());
        when(mockContestRepository.existsByTitleEqualsIgnoreCase(anyString())).thenReturn(false);
        when(mockContestRepository.save(mockContest)).thenReturn(mockContest);

        // Act
        contestServices.create(mockContest, mockOrganizer);

        // Assert
        verify(mockContestRepository).save(mockContest);
    }

    @Test
    public void create_Should_CallRepository_WithAddedJuries() {
        // Arrange
        Contest mockContest = createMockContestDynamic();
        User mockOrganizer = createMockOrganizer();
        User mockJury = createMockUser();
        mockJury.setRank(createMockRanking(Ranks.MASTER));
        Set<User> juries = new HashSet<>();
        juries.add(mockJury);
        mockContest.setJuries(juries);
        mockContest.setPhotos(new HashSet<>());

        when(mockUserServices.getAllOrganizers()).thenReturn(new ArrayList<>());
        when(mockUserServices.getUsersWithJuryPermission()).thenReturn(List.of(mockJury));
        when(mockContestRepository.existsByTitleEqualsIgnoreCase(anyString())).thenReturn(false);
        when(mockContestRepository.save(mockContest)).thenReturn(mockContest);

        // Act
        contestServices.create(mockContest, mockOrganizer);

        // Assert
        verify(mockContestRepository, times(2)).save(mockContest);
    }

    @Test
    public void create_Should_ThrowException_When_UserIsNotOrganizer() {
        // Arrange
        Contest mockContest = createMockContest();
        User mockUser = createMockUser();

        // Act, Assert
        assertThrows(UnauthorizedOperationException.class,
                () -> contestServices.create(mockContest, mockUser));
    }

    @Test
    public void create_Should_ThrowException_When_ContestWithSameNameExist() {
        // Arrange
        Contest mockContest = createMockContest();
        User mockOrganizer = createMockOrganizer();

        when(mockContestRepository.existsByTitleEqualsIgnoreCase(anyString())).thenReturn(true);

        // Act, Assert
        assertThrows(EntityDuplicateException.class,
                () -> contestServices.create(mockContest, mockOrganizer));
    }

    @Test
    public void create_Should_ThrowException_When_ContestPhasesAreWrong() {
        // Arrange
        Contest mockContest1 = createMockContest();
        Contest mockContest2 = createMockContest();
        User mockOrganizer = createMockOrganizer();
        mockContest1.setPhase1(LocalDateTime.now());
        mockContest2.setPhase2(LocalDateTime.now());

        // Act, Assert
        assertAll(() -> {
            assertThrows(DateTimeException.class,
                    () -> contestServices.create(mockContest1, mockOrganizer));
            assertThrows(DateTimeException.class,
                    () -> contestServices.create(mockContest2, mockOrganizer));
        });
    }

    @Test
    public void join_Should_CallRepository_When_UserIsNotAlreadyEnlisted() {
        // Arrange
        Contest mockContest = createMockContestDynamic();
        User mockUser = createMockUser();
        User mockDifferentUser = createDifferentMockUser();
        Ranking mockRanking = createMockRanking(Ranks.JUNKIE);
        Set<User> participants = new HashSet<>();
        participants.add(mockDifferentUser);
        mockContest.setParticipants(participants);

        when(mockContestRepository.save(mockContest)).thenReturn(mockContest);
        when(mockRankingService.getJunkie()).thenReturn(mockRanking);

        // Act
        contestServices.join(mockContest, mockUser);

        // Assert
        verify(mockContestRepository).save(mockContest);
    }

    @Test
    public void join_Should_ThrowException_When_ContestIsInvitational() {
        // Arrange
        Contest mockContest = createMockContest();
        mockContest.setInvitational(true);
        User mockUser = createMockUser();

        // Act, Assert
        assertThrows(UnauthorizedOperationException.class,
                () -> contestServices.join(mockContest, mockUser));
    }

    @Test
    public void join_Should_ThrowException_When_EnrolmentIsNotInTimeLimits() {
        // Arrange
        Contest mockContest = createMockContestDynamic();
        mockContest.setInvitational(false);
        mockContest.setParticipants(new HashSet<>());
        mockContest.setPhase1(LocalDateTime.now().minusDays(1));
        User mockUser = createMockUser();

        // Act, Assert
        assertThrows(UnauthorizedOperationException.class,
                () -> contestServices.join(mockContest, mockUser));
    }
}
