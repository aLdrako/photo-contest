package com.telerikacademy.web.photocontest.repositories.contracts;

import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.models.Photo;
import com.telerikacademy.web.photocontest.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    boolean existsByUserCreatedAndPostedOn(User user, Contest contest);
    @Query("select sum(ps.score) from PhotoScore ps where ps.reviewId.photoId.id = :id")
    int getScoreOfPhoto(@Param("id") Long photoId);
    List<Photo> findAllByPostedOn(Contest contest);

    //@Query("from Photo p where p.title like %:title% and p.postedOn.id = :id")
    //List<Photo> search(@Param("title") Optional<String> keyword,
    //                   @Param("id") Optional<Long> contestId);
    //@Query("from Photo p where p.title like %:title%")
    //List<Photo> search(@Param("title") Optional<String> keyword);

    @Query("select p from Photo p where (:title is null or p.title like %:title%) and " +
            "(:contestId is null or p.postedOn.id = :contestId) and (:userId is null or p.userCreated.id = :userId)")
    Page<Photo> search(@Param("title") String title, @Param("contestId") Long contestId,
                       @Param("userId") Long userId,
                       Pageable pageable);
}
