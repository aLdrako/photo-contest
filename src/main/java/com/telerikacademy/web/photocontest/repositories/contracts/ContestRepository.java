package com.telerikacademy.web.photocontest.repositories.contracts;
import com.telerikacademy.web.photocontest.models.Contest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContestRepository extends JpaRepository<Contest, Long> {
    @Query("""
        SELECT c FROM Contest c WHERE
        (:title IS NULL OR c.title LIKE %:title%) AND
        (:categoryName IS NULL OR c.category.name = :categoryName) AND
        (:isInvitational IS NULL OR c.isInvitational = :isInvitational)
        """)
    List<Contest> filter(
            @Param("title") String title,
            @Param("categoryName") String categoryName,
            @Param("isInvitational") Boolean isInvitational
    );
    boolean existsByTitleEqualsIgnoreCase(String title);
}
