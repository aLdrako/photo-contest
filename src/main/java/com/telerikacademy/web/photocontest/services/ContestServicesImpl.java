package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.exceptions.EntityDuplicateException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.repositories.contracts.ContestRepository;
import com.telerikacademy.web.photocontest.services.contracts.ContestServices;
import com.telerikacademy.web.photocontest.services.contracts.UserServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ContestServicesImpl implements ContestServices {
    private static final String UNAUTHORIZED_MANIPULATION_MESSAGE = "Only users with Organizer role can create, update, delete contests or add juries and participants!";
    private static final String USER_ALREADY_IN_LIST_MESSAGE = "Current user already in the list of participants or juries!";
    private static final String USER_AS_JURY_NOT_ELIGIBLE_MESSAGE = "Only users with ranking 'Master' or above are eligible to be selected as jury!";
    private static final String JURI_AS_PARTICIPANT_NOT_ELIGIBLE_MESSAGE = "Juries cannot participate within the same contest where they are jury!";
    private static final String PHASE_1_VALIDATION_MESSAGE = "Phase 1 should be in the future in bounds of one day to one month";
    private static final String PHASE_2_VALIDATION_MESSAGE = "Phase 2 should be after Phase 1 in bounds of one hour to one day";
    private static final String INVITATIONAL_CONTEST_MESSAGE = "This contest is Invitational, only Organizers can invite participants";
    private static final String ENROLL_INVITATION_TIME_LIMITS_MESSAGE = "Users can enroll / be invited only during Phase 1";

    private final ContestRepository contestRepository;

    private final UserServices userServices;

    @Override
    public Iterable<Contest> findAll() {
        return contestRepository.findAll();
    }

    @Override
    public Contest findById(Long id) {
        return contestRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Contest", id));
    }

    @Override
    public Contest create(Contest contest, User authenticatedUser) {
        checkOrganizerPermissions(authenticatedUser);
        checkUniqueness(contest.getTitle());
        validatePhases(contest);
        Set<User> juries = contest.getJuries();
        contest.setJuries(new HashSet<>());
        contest.setParticipants(new HashSet<>());
        juries.forEach(user -> addJury(contest, authenticatedUser, user.getUsername()));
        juries.addAll(userServices.getAllOrganizers());
        contest.setJuries(juries);
        return contestRepository.save(contest);
    }

    @Override
    public Contest update(Contest contest, User authenticatedUser) {
        checkOrganizerPermissions(authenticatedUser);
        return contestRepository.save(contest);
    }

    @Override
    public Contest uploadCover(Contest contest, User authenticatedUser) {
        checkOrganizerPermissions(authenticatedUser);
        return contestRepository.save(contest);
    }

    @Override
    public void deleteById(Long id, User authenticatedUser) {
        checkOrganizerPermissions(authenticatedUser);
        contestRepository.deleteById(findById(id).getId());
    }

    @Override
    public List<Contest> filter(String title, String categoryName, Boolean type) {
        return contestRepository.filter(title, categoryName, type);
    }

    @Override
    public Contest join(Contest contest, User authenticatedUser) {
        isInvitational(contest);
        isInEnrollTimeLimits(contest);
        Set<User> participants = contest.getParticipants();
        checkIfEnlisted(participants, authenticatedUser.getUsername());
        checkIfIsJury(contest, authenticatedUser);
        authenticatedUser.setPoints(authenticatedUser.getPoints() + 1);
        participants.add(authenticatedUser);
        contest.setParticipants(participants);
        return contestRepository.save(contest);
    }

    @Override
    public Contest addParticipant(Contest contest, User authenticatedUser, String username) {
        checkOrganizerPermissions(authenticatedUser);
        isInEnrollTimeLimits(contest);
        User participantToAdd = userServices.getByUsername(username);
        Set<User> participants = contest.getParticipants();
        checkIfEnlisted(participants, participantToAdd.getUsername());
        checkIfIsJury(contest, participantToAdd);
        participantToAdd.setPoints(participantToAdd.getPoints() + 3);
        participants.add(participantToAdd);
        contest.setParticipants(participants);
        return contestRepository.save(contest);
    }

    @Override
    public Contest addJury(Contest contest, User authenticatedUser, String username) {
        checkOrganizerPermissions(authenticatedUser);
        isInEnrollTimeLimits(contest);
        User juryToAdd = userServices.getByUsername(username);
        Set<User> juries = contest.getJuries();
        Set<User> participants = contest.getParticipants();
        checkIfEnlisted(juries, username);
        checkIfEnlisted(participants, username);
        checkIfEligibleJury(username);
        juries.add(juryToAdd);
        contest.setJuries(juries);
        return contestRepository.save(contest);
    }

    @Override
    public void checkUniqueness(String title) {
        if (contestRepository.existsByTitleEqualsIgnoreCase(title)) {
            throw new EntityDuplicateException("Contest", "title", title);
        }
    }

    private void checkOrganizerPermissions(User authenticatedUser) {
        if (!authenticatedUser.isOrganizer()) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_MANIPULATION_MESSAGE);
        }
    }

    private static void isInvitational(Contest contest) {
        if (contest.isInvitational()) {
            throw new UnauthorizedOperationException(INVITATIONAL_CONTEST_MESSAGE);
        }
    }

    private static void isInEnrollTimeLimits(Contest contest) {
        if (LocalDateTime.now().isAfter(contest.getPhase1())) {
            throw new UnauthorizedOperationException(ENROLL_INVITATION_TIME_LIMITS_MESSAGE);
        }
    }

    private static void checkIfEnlisted(Set<User> participantsOrJuries, String username) {
        if (participantsOrJuries.stream().anyMatch(user -> user.getUsername().equals(username))) {
            throw new EntityDuplicateException(USER_ALREADY_IN_LIST_MESSAGE);
        }
    }

    private static void checkIfIsJury(Contest contest, User authenticatedUser) {
        if (contest.getJuries().stream().anyMatch(user -> user.getUsername().equals(authenticatedUser.getUsername()))) {
            throw new UnauthorizedOperationException(JURI_AS_PARTICIPANT_NOT_ELIGIBLE_MESSAGE);
        }
    }

    private void checkIfEligibleJury(String username) {
        if (userServices.getUsersWithJuryPermission().stream().noneMatch(user -> user.getUsername().equals(username))) {
            throw new UnauthorizedOperationException(USER_AS_JURY_NOT_ELIGIBLE_MESSAGE);
        }
    }

    private void validatePhases(Contest contest) {
        LocalDateTime dateCreated = contest.getDateCreated() != null ? contest.getDateCreated() : LocalDateTime.now();
        if (!(contest.getPhase1().isAfter(dateCreated.plusDays(1)) && contest.getPhase1().isBefore(dateCreated.plusMonths(1)))) {
            throw new DateTimeException(PHASE_1_VALIDATION_MESSAGE);
        }
        if (!(contest.getPhase2().isAfter(contest.getPhase1().plusHours(1)) && contest.getPhase2().isBefore(contest.getPhase1().plusDays(1)))) {
            throw new DateTimeException(PHASE_2_VALIDATION_MESSAGE);
        }
    }
}
