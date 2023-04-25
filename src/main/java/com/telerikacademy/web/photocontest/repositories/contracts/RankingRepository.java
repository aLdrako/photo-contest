package com.telerikacademy.web.photocontest.repositories.contracts;

import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.models.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RankingRepository extends JpaRepository<Ranking, Integer> {

    Optional<Ranking> findRankingByName(String name);

    default Ranking getByName(String name) {
        return findRankingByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Ranking", "name", name));
    }

}
