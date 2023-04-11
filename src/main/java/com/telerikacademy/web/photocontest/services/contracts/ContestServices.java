package com.telerikacademy.web.photocontest.services.contracts;

import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.models.User;

import java.util.List;

public interface ContestServices {
    Iterable<Contest> findAll();
    Contest findById(Long id);
    Contest create(Contest contest, User authenticatedUser);
    Contest update(Contest contest, User authenticatedUser);
    Contest uploadCover(Contest contest, User authenticatedUser);
    void deleteById(Long id, User authenticatedUser);
    List<Contest> filter(String title, String categoryName, Boolean type);
    Contest join(Contest contest, User authenticatedUser);
    Contest addJury(Contest contest, User authenticatedUser, String username);
    Contest addParticipant(Contest contest, User authenticatedUser, String username);
    void checkUniqueness(String title);
}
