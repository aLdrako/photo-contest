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
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ContestServicesImpl implements ContestServices {
    private static final String UNAUTHORIZED_MANIPULATION_MESSAGE = "Only users with Organizer role can create, update, delete contests or add juries!";
    private static final String USER_ALREADY_IN_LIST_MESSAGE = "Current user already in the list of participants or juries!";
    private static final String USER_AS_JURY_NOT_ELIGIBLE_MESSAGE = "Current user is not eligible for jury role!";
    private static final String JURI_AS_PARTICIPANT_NOT_ELIGIBLE_MESSAGE = "Juries cannot participate within the same contest where they are jury!";

    private final ContestRepository contestRepository;

    private final UserServices userServices;

    public Iterable<Contest> findAll() {
        return contestRepository.findAll();
    }

    public Contest findById(Long id) {
        return contestRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Contest", id));
    }

    public Contest save(Contest contest, User authenticatedUser) {
        checkOrganizerPermissions(authenticatedUser);
        checkUniqueness(contest);
        validatePhases(contest);
        return contestRepository.save(contest);
    }

    private void validatePhases(Contest contest) {
        LocalDateTime dateCreated = contest.getDateCreated() != null ? contest.getDateCreated() : LocalDateTime.now();
        if (!(contest.getPhase1().isAfter(dateCreated.plusDays(1)) && contest.getPhase1().isBefore(dateCreated.plusMonths(1)))) {
            throw new DateTimeException("Phase 1 should be in the future in bounds of one day to one month");
        }
        if (!(contest.getPhase2().isAfter(contest.getPhase1().plusHours(1)) && contest.getPhase2().isBefore(contest.getPhase1().plusDays(1)))) {
            throw new DateTimeException("Phase 2 should be after Phase 1 in bounds of one hour to one day");
        }
    }

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
        Set<User> participants = contest.getParticipants();
        if (participants.stream().anyMatch(user -> user.equals(authenticatedUser))) {
            throw new EntityDuplicateException(USER_ALREADY_IN_LIST_MESSAGE);
        }
        if (contest.getJuries().stream().anyMatch(user -> user.equals(authenticatedUser))) {
            throw new UnauthorizedOperationException(JURI_AS_PARTICIPANT_NOT_ELIGIBLE_MESSAGE);
        }
        participants.add(authenticatedUser);
        contest.setParticipants(participants);
        return contestRepository.save(contest);
    }

    @Override
    public Contest addJury(Contest contest, User authenticatedUser, String username) {
        checkOrganizerPermissions(authenticatedUser);
        User juryToAdd = userServices.getByUsername(username);
        Set<User> juries = contest.getJuries();
        if (juries.stream().anyMatch(user -> user.getUsername().equals(username))) {
            throw new EntityDuplicateException(USER_ALREADY_IN_LIST_MESSAGE);
        }
        if (userServices.getUsersWithJuryPermission().stream().noneMatch(user -> user.getUsername().equals(username))) {
            throw new UnauthorizedOperationException(USER_AS_JURY_NOT_ELIGIBLE_MESSAGE);
        }
        juries.add(juryToAdd);
        contest.setJuries(juries);
        return contestRepository.save(contest);
    }

    private void checkUniqueness(Contest contest) {
        if (contestRepository.existsByTitleEqualsIgnoreCase(contest.getTitle())) {
            throw new EntityDuplicateException("Contest", "title", contest.getTitle());
        }
    }

    private void checkOrganizerPermissions(User authenticatedUser) {
        if (!authenticatedUser.isOrganizer()) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_MANIPULATION_MESSAGE);
        }
    }
}
