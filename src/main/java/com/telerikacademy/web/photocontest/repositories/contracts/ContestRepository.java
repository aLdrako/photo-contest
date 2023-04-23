package com.telerikacademy.web.photocontest.repositories.contracts;
import com.telerikacademy.web.photocontest.models.Contest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {
    @Query("""
        SELECT c FROM Contest c WHERE
        (:title IS NULL OR c.title LIKE %:title%) AND
        (:categoryName IS NULL OR c.category.name = :categoryName) AND
        (:isInvitational IS NULL OR c.isInvitational = :isInvitational) AND
        (:isFinished IS NULL OR c.isFinished = :isFinished) AND
        (:phase IS NULL OR (
            (:phase = 'phase1' AND (c.dateCreated <= :now AND c.phase1 >= :now)) OR
            (:phase = 'phase2' AND (c.phase1 <= :now AND c.phase2 >= :now))
        ))
    """)
Page<Contest> filter(
            @Param("title") String title,
            @Param("categoryName") String categoryName,
            @Param("isInvitational") Boolean isInvitational,
            @Param("isFinished") Boolean isFinished,
            @Param("phase") String phase,
            @Param("now") LocalDateTime now,
            Pageable pageable
    );

    boolean existsByTitleEqualsIgnoreCase(String title);
    List<Contest> findByPhase2IsBeforeAndIsFinishedFalse(LocalDateTime now);
    List<Contest> findTop5ByIsFinishedTrue();
}
