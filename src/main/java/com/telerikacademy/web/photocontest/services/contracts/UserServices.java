package com.telerikacademy.web.photocontest.services.contracts;

import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.models.dto.PermissionsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserServices {
    List<User> getAll();
    Page<User> findAll(Pageable pageable, boolean withOrganizers);
    void update(User user, User userFromAuthorization);
    void create(User user);
    User getById(Long id);
    User getByUsername(String username);
    List<User> getAllOrganizers();
    List<User> getUsersWithJuryPermission();
    void delete(Long id, User userFromAuthorization);
    Page<User> search(Optional<String> keyword, Pageable pageable);
    void updatePermissions(User userFromRepo, User userFromAuthorization, PermissionsDto permissionsDto);

    //Page<User> findAllPhotoJunkies(Pageable pageable);
}
