package com.telerikacademy.web.photocontest.repositories.contracts;

import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.models.ContestResults;
import com.telerikacademy.web.photocontest.models.Photo;
import com.telerikacademy.web.photocontest.models.ResultEmbed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestResultsRepository extends JpaRepository<ContestResults, Long> {
    void deleteContestResultsByResultEmbed_Contest(Contest contest);
    void deleteContestResultsByResultEmbed_Photo(Photo photo);
}
