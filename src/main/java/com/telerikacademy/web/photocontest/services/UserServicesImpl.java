package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.exceptions.EntityDuplicateException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.models.Ranking;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.models.dto.PermissionsDto;
import com.telerikacademy.web.photocontest.repositories.contracts.RankingRepository;
import com.telerikacademy.web.photocontest.repositories.contracts.UserRepository;
import com.telerikacademy.web.photocontest.services.contracts.RankingServices;
import com.telerikacademy.web.photocontest.services.contracts.UserServices;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class UserServicesImpl implements UserServices {
    private static final String UNAUTHORIZED_UPDATE_DELETE_MESSAGE = "Only an organizer or owner of " +
            "account can update/delete profile!";
    private static final String UNAUTHORIZED_UPDATE_PERMISSIONS_MESSAGE = "Only organizers can change permissions!";
    private final UserRepository userRepository;
    private final RankingServices rankingServices;

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }
    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }


    @Override
    public void update(User user, User userFromAuthorization) {
        checkAuthorizationPermissions(user, userFromAuthorization);
        checkForDuplicateEmail(user);
        userRepository.update(user);
    }

    private void checkAuthorizationPermissions(User user, User userFromAuthorization) {
        if (!userFromAuthorization.isOrganizer()  &&
                !user.getUsername().equals(userFromAuthorization.getUsername())) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_UPDATE_DELETE_MESSAGE);
        }
    }

    @Override
    public void create(User user) {
        checkForDuplicateEmail(user);
        checkForDuplicateUsername(user);
        userRepository.create(user);
    }

    private void checkForDuplicateUsername(User user) {
        boolean duplicate = true;
        try {
            User userFromRepo = userRepository.getByUsername(user.getUsername());
            if (userFromRepo.getId().equals(user.getId())) {
                duplicate = false;
            }
        } catch (EntityNotFoundException e) {
            duplicate = false;
        }
        if (duplicate) {
            throw new EntityDuplicateException("User", "username", user.getUsername());
        }
    }

    private void checkForDuplicateEmail(User user) {
        boolean duplicate = true;
        try {
            User userFromRepo = userRepository.getByEmail(user.getEmail());
            if (userFromRepo.getId().equals(user.getId())) {
                duplicate = false;
            }
        } catch (EntityNotFoundException e) {
            duplicate = false;
        }
        if (duplicate) {
            throw new EntityDuplicateException("User", "email", user.getEmail());
        }
    }

    @Override
    public User getById(Long id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }
    @Override
    public List<User> getAllOrganizers() {
        return userRepository.getAllOrganizers();
    }

    @Override
    public List<User> getUsersWithJuryPermission() {
        return userRepository.getUsersWithJuryPermission();
    }

    @Override
    public void delete(Long id, User userFromAuthorization) {
        User userFromRepo = userRepository.getById(id);
        checkAuthorizationPermissions(userFromRepo, userFromAuthorization);
        changeUserProperties(userFromRepo);
        userRepository.update(userFromRepo);
    }

    @Override
    public List<User> search(Optional<String> keyword) {
        return userRepository.search(keyword);
    }

    @Override
    public void updatePermissions(User userFromRepo, User userFromAuthorization,
                                  PermissionsDto permissionsDto) {
        if (!userFromAuthorization.isOrganizer()) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_UPDATE_PERMISSIONS_MESSAGE);
        }
        userFromRepo.setOrganizer(permissionsDto.getOrganiser());
        userRepository.update(userFromRepo);
    }

    private void changeUserProperties(User userFromRepo) {
        userFromRepo.setDeleted(true);
        userFromRepo.setOrganizer(false);
        userFromRepo.setFirstName(generateString());
        userFromRepo.setLastName(generateString());
        userFromRepo.setUsername(generateString());
        userFromRepo.setPassword(generateString());
        userFromRepo.setEmail(generateString());
        userFromRepo.setPoints(-1);
        userFromRepo.setRank(rankingServices.getJunkie());
    }
    private String generateString() {
        return new Random().ints(48, 123)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)).limit(15)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }

}
