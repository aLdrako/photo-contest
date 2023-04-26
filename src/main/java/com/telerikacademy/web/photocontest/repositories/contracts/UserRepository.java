package com.telerikacademy.web.photocontest.repositories.contracts;

import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    List<User> findAllByIsDeletedFalse();
    Optional<User> findByIdAndIsDeletedIsFalse(Long id);
    default User getById(Long id) {
        return findByIdAndIsDeletedIsFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("User", id));
    }

    Optional<User> findByEmailAndIsDeletedIsFalse(String email);
    default User getByEmail(String email) {
        return findByEmailAndIsDeletedIsFalse(email)
                .orElseThrow(() -> new EntityNotFoundException("User", "email", email));
    }

    Optional<User> findByUsernameAndIsDeletedIsFalse(String username);
    default User getByUsername(String username) {
        return findByUsernameAndIsDeletedIsFalse(username)
                .orElseThrow(() -> new EntityNotFoundException("User", "username", username));
    }
    @Query("from User where isOrganizer = true and isDeleted = false")
    List<User> getAllOrganizers();

    @Query("""
           from User where (rank.name = 'Master' or rank.name = 'Wise and Benevolent Photo Dictator')
           and isDeleted = false
           """)
    List<User> getUsersWithJuryPermission();
    @Query("""
           SELECT u from User u where :keyword is null or (u.firstName = :keyword or u.lastName = :keyword
           or u.username = :keyword) and u.isDeleted = false
           """)
    Page<User> searchAll(@Param("keyword") String keyword, Pageable pageable);
    @Query("""
           SELECT u from User u where :keyword is null or (u.firstName = :keyword or u.lastName = :keyword
           or u.username = :keyword) and u.isDeleted = false and u.isOrganizer = false
           """)
    Page<User> searchPhotoJunkie(@Param("keyword") String keyword, Pageable pageable);

}
