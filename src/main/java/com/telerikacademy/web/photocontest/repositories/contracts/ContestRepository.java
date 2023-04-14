package com.telerikacademy.web.photocontest.repositories.contracts;
import com.telerikacademy.web.photocontest.models.Contest;

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
        (:phase1 IS NULL OR (c.dateCreated <= :phase1 AND c.phase1 >= :phase1)) AND
        (:phase2 IS NULL OR (c.phase1 <= :phase2 AND c.phase2 >= :phase2))
        """)
    List<Contest> filter(
            @Param("title") String title,
            @Param("categoryName") String categoryName,
            @Param("isInvitational") Boolean isInvitational,
            @Param("isFinished") Boolean isFinished,
            @Param("phase1") LocalDateTime phase1,
            @Param("phase2") LocalDateTime phase2
    );

    default List<Contest> filterWithDefault() {
        return filter(null, null, null, null, null, null);
    }
    boolean existsByTitleEqualsIgnoreCase(String title);
    List<Contest> findByPhase2IsBeforeAndIsFinishedFalse(LocalDateTime now);
}
