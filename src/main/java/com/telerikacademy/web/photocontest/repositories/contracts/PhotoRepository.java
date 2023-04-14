package com.telerikacademy.web.photocontest.repositories.contracts;

import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.models.Photo;
import com.telerikacademy.web.photocontest.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    boolean existsByUserCreatedAndPostedOn(User user, Contest contest);
    @Query("select sum(ps.score) from PhotoScore ps where ps.reviewId.photoId.id = :id")
    int getScoreOfPhoto(@Param("id") Long photoId);
    List<Photo> findAllByPostedOn(Contest contest);
}
