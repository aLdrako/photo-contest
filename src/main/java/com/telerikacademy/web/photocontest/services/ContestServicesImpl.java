package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.exceptions.EntityDuplicateException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.repositories.contracts.ContestRepository;
import com.telerikacademy.web.photocontest.services.contracts.ContestServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ContestServicesImpl implements ContestServices {
    private static final String UNAUTHORIZED_MANIPULATION_MESSAGE = "Only users with Organizer role can create, update or delete contests!";
    private final ContestRepository contestRepository;

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
