package com.telerikacademy.web.photocontest.repositories.contracts;

import com.telerikacademy.web.photocontest.models.Ranking;

public interface RankingRepository {
    Ranking getByName(String name);

    void create(Ranking ranking);
}
