package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.exceptions.EntityDuplicateException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.models.*;
import com.telerikacademy.web.photocontest.repositories.contracts.ContestRepository;
import com.telerikacademy.web.photocontest.repositories.contracts.ContestResultsRepository;
import com.telerikacademy.web.photocontest.services.contracts.RankingServices;
import com.telerikacademy.web.photocontest.services.contracts.UserServices;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
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
    @Mock
    ContestResultsRepository contestResultsRepository;
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
    public void update_Should_CallRepository_When_UserIsOrganizer() throws FileUploadException {
        // Arrange
        Contest mockContest = createMockContestDynamic();
        User mockOrganizer = createMockOrganizer();

        // Act
        contestServices.update(mockContest, mockOrganizer, createMockFile());

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
                () -> contestServices.update(mockContest, mockUser, createMockFile()));
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
        Contest mockContest = createMockContestDynamic();
        User mockOrganizer = createMockOrganizer();

        when(mockContestRepository.findById(mockContest.getId())).thenReturn(Optional.of(mockContest));
        doNothing().when(contestResultsRepository).deleteContestResultsByResultEmbed_Contest(mockContest);

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
        contestServices.filter(anyString(), anyString(), anyBoolean(), anyBoolean(), any(), any(), any());

        // Assert
        verify(mockContestRepository).filter(anyString(), anyString(), anyBoolean(), anyBoolean(), any(), any(), any());
    }

    @Test
    public void create_Should_CallRepository_WithoutAddedJuries() throws FileUploadException {
        // Arrange
        Contest mockContest = createMockContestDynamic();
        User mockOrganizer = createMockOrganizer();
        mockContest.setJuries(null);

        when(mockUserServices.getAllOrganizers()).thenReturn(new ArrayList<>());
        when(mockContestRepository.existsByTitleEqualsIgnoreCase(anyString())).thenReturn(false);
        when(mockContestRepository.save(mockContest)).thenReturn(mockContest);

        // Act
        contestServices.create(mockContest, mockOrganizer, createMockFile());

        // Assert
        verify(mockContestRepository).save(mockContest);
    }

    @Test
    public void create_Should_CallRepository_WithAddedJuries() throws FileUploadException {
        // Arrange
        Contest mockContest = createMockContestDynamic();
        User mockOrganizer = createMockOrganizer();
        User mockUser = createMockUser();
        Set<User> juries = new HashSet<>();
        juries.add(mockUser);
        mockContest.setJuries(juries);
        mockContest.setPhotos(new HashSet<>());

        when(mockUserServices.getUsersWithJuryPermission()).thenReturn(List.of(mockUser));
        when(mockContestRepository.save(mockContest)).thenReturn(mockContest);

        // Act
        contestServices.create(mockContest, mockOrganizer, createMockFile());

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
                () -> contestServices.create(mockContest, mockUser, createMockFile()));
    }

    @Test
    public void create_Should_ThrowException_When_ContestWithSameNameExist() {
        // Arrange
        Contest mockContest = createMockContest();
        User mockOrganizer = createMockOrganizer();

        when(mockContestRepository.existsByTitleEqualsIgnoreCase(anyString())).thenReturn(true);

        // Act, Assert
        assertThrows(EntityDuplicateException.class,
                () -> contestServices.create(mockContest, mockOrganizer, createMockFile()));
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
                    () -> contestServices.create(mockContest1, mockOrganizer, createMockFile()));
            assertThrows(DateTimeException.class,
                    () -> contestServices.create(mockContest2, mockOrganizer, createMockFile()));
        });
    }

    @Test
    public void create_Should_ThrowException_When_AddingUserAsJuryWhichIsNotEligible() {
        // Arrange
        Contest mockContest = createMockContestDynamic();
        User mockOrganizer = createMockOrganizer();
        User mockUser = createMockUser();
        User mockDifferentUser = createDifferentMockUser();
        Set<User> juries = new HashSet<>();
        juries.add(mockUser);
        mockContest.setJuries(juries);
        mockContest.setPhotos(new HashSet<>());
        when(mockUserServices.getUsersWithJuryPermission()).thenReturn(List.of(mockDifferentUser));

        // Act, Assert
        assertThrows(UnauthorizedOperationException.class,
                ()  ->  contestServices.create(mockContest, mockOrganizer, createMockFile()));
    }

    @Test
    public void join_Should_CallRepository_When_UserIsNotEnlisted() {
        // Arrange
        Contest mockContest = createMockContestDynamic();
        User mockUser = createMockUser();
        User mockDifferentUser = createDifferentMockUser();
        Set<User> participants = new HashSet<>();
        participants.add(mockDifferentUser);
        mockContest.setParticipants(participants);

        when(mockContestRepository.save(mockContest)).thenReturn(mockContest);

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
        User mockUser = createMockUser();
        mockContest.setInvitational(false);
        mockContest.setParticipants(new HashSet<>());
        mockContest.setPhase1(LocalDateTime.now().minusDays(1));

        // Act, Assert
        assertThrows(UnauthorizedOperationException.class,
                () -> contestServices.join(mockContest, mockUser));
    }

    @Test
    public void join_Should_ThrowException_When_ParticipantAlreadyEnlisted() {
        // Arrange
        Contest mockContest = createMockContestDynamic();
        User mockUser = createMockUser();
        Set<User> participants = new HashSet<>();
        participants.add(mockUser);
        mockContest.setParticipants(participants);

        // Act, Assert
        assertThrows(EntityDuplicateException.class,
                () -> contestServices.join(mockContest, mockUser));
    }

    @Test
    public void join_Should_ThrowException_When_ParticipantIsInJuryList() {
        // Arrange
        Contest mockContest = createMockContestDynamic();
        User mockUser = createMockUser();
        Set<User> juries = new HashSet<>();
        juries.add(mockUser);
        mockContest.setJuries(juries);

        // Act, Assert
        assertThrows(UnauthorizedOperationException.class,
                () -> contestServices.join(mockContest, mockUser));
    }

    @Test
    public void join_Should_AddPointsToUser_When_HeJoinContest() {
        // Arrange
        Contest mockContest = createMockContestDynamic();
        User mockUser = createMockUser();
        User mockDifferentUser = createDifferentMockUser();
        Set<User> participants = new HashSet<>();
        participants.add(mockDifferentUser);
        mockContest.setParticipants(participants);

        int initialPoints = mockUser.getPoints();

        // Act
        contestServices.join(mockContest, mockUser);

        // Assert
        assertEquals(initialPoints + 1, mockUser.getPoints());
    }

    @Test
    public void addParticipant_Should_CallRepository_When_UserIsNotEnlisted()  {
        // Arrange
        Contest mockContest = createMockContestDynamic();
        User mockDifferentUser = createDifferentMockUser();
        User mockOrganizer = createMockOrganizer();
        Set<User> participants = new HashSet<>();
        participants.add(mockOrganizer);
        mockContest.setParticipants(participants);

        when(mockContestRepository.save(mockContest)).thenReturn(mockContest);
        when(mockUserServices.getByUsername(mockDifferentUser.getUsername())).thenReturn(mockDifferentUser);

        // Act
        contestServices.addParticipant(mockContest, mockOrganizer, mockDifferentUser.getUsername());

        // Assert
        verify(mockContestRepository).save(mockContest);
    }

    @Test
    public void addParticipant_Should_ThrowException_When_EnrolmentIsNotInTimeLimits()  {
        // Arrange
        Contest mockContest = createMockContestDynamic();
        User mockUser = createMockUser();
        User mockOrganizer = createMockUser();
        mockContest.setParticipants(new HashSet<>());
        mockContest.setPhase1(LocalDateTime.now().minusDays(1));

        // Act, Assert
        assertThrows(UnauthorizedOperationException.class,
                () -> contestServices.addParticipant(mockContest, mockOrganizer, mockUser.getUsername()));
    }

    @Test
    public void addParticipant_Should_ThrowException_When_ParticipantAlreadyEnlisted()  {
        // Arrange
        Contest mockContest = createMockContestDynamic();
        User mockUser = createMockUser();
        User mockOrganizer = createMockOrganizer();
        Set<User> participants = new HashSet<>();
        participants.add(mockUser);
        mockContest.setParticipants(participants);

        when(mockUserServices.getByUsername(mockUser.getUsername())).thenReturn(mockUser);

        // Act, Assert
        assertThrows(EntityDuplicateException.class,
                () -> contestServices.addParticipant(mockContest, mockOrganizer, mockUser.getUsername()));
    }

    @Test
    public void addParticipant_Should_ThrowException_When_ParticipantIsInJuryList() {
        // Arrange
        Contest mockContest = createMockContestDynamic();
        User mockUser = createMockUser();
        User mockOrganizer = createMockOrganizer();
        Set<User> juries = new HashSet<>();
        juries.add(mockUser);
        mockContest.setJuries(juries);

        when(mockUserServices.getByUsername(mockUser.getUsername())).thenReturn(mockUser);

        // Act, Assert
        assertThrows(UnauthorizedOperationException.class,
                () -> contestServices.addParticipant(mockContest, mockOrganizer, mockUser.getUsername()));
    }

    @Test
    public void addParticipant_Should_ThrowException_When_UserIsNotOrganizer() {
        // Arrange
        Contest mockContest = createMockContest();
        User mockUser = createMockUser();
        User mockDifferentUser = createDifferentMockUser();

        // Act, Assert
        assertThrows(UnauthorizedOperationException.class,
                () -> contestServices.addParticipant(mockContest, mockUser, mockDifferentUser.getUsername()));
    }

    @Test
    public void addParticipant_Should_AddPointsToUser_When_HeJoinContest() {
        // Arrange
        Contest mockContest = createMockContestDynamic();
        User mockUser = createMockUser();
        User mockOrganizer = createMockOrganizer();
        User mockDifferentUser = createDifferentMockUser();
        Set<User> participants = new HashSet<>();
        participants.add(mockDifferentUser);
        mockContest.setParticipants(participants);

        int initialPoints = mockUser.getPoints();
        when(mockUserServices.getByUsername(mockUser.getUsername())).thenReturn(mockUser);

        // Act
        contestServices.addParticipant(mockContest, mockOrganizer, mockUser.getUsername());

        // Assert
        assertEquals(initialPoints + 3, mockUser.getPoints());
    }

    @Test
    public void evaluateRank_Should_ChangeRankBasedOnPoints()  {
        // Arrange
        User mockUser = createMockUser();
        Ranking mockRankingJunkie = createMockRanking(Ranks.JUNKIE);
        Ranking mockRankingEnthusiast = createMockRanking(Ranks.ENTHUSIAST);
        Ranking mockRankingMaster = createMockRanking(Ranks.MASTER);
        Ranking mockRankingWiseDictator = createMockRanking(Ranks.WISE_AND_BENEVOLENT_PHOTO_DICTATOR);

        when(mockRankingService.getJunkie()).thenReturn(mockRankingJunkie);
        when(mockRankingService.getEnthusiast()).thenReturn(mockRankingEnthusiast);
        when(mockRankingService.getMaster()).thenReturn(mockRankingMaster);
        when(mockRankingService.getWiseAndBenevolentPhotoDictator()).thenReturn(mockRankingWiseDictator);

        // Arrange
        mockUser.setPoints(0);
        // Act
        contestServices.evaluateRank(mockUser);
        // Assert
        assertEquals(mockRankingJunkie, mockUser.getRank());

        // Arrange
        mockUser.setPoints(51);
        // Act
        contestServices.evaluateRank(mockUser);
        // Assert
        assertEquals(mockRankingEnthusiast, mockUser.getRank());

        // Arrange
        mockUser.setPoints(151);
        // Act
        contestServices.evaluateRank(mockUser);
        // Assert
        assertEquals(mockRankingMaster, mockUser.getRank());

        // Arrange
        mockUser.setPoints(1001);
        // Act
        contestServices.evaluateRank(mockUser);
        // Assert
        assertEquals(mockRankingWiseDictator, mockUser.getRank());
    }
}
