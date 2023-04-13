package com.telerikacademy.web.photocontest.helpers;

import com.telerikacademy.web.photocontest.models.Ranking;
import com.telerikacademy.web.photocontest.models.Ranks;

public class Helpers {


    public static Ranking createMockRanking(Ranks rank) {
        Ranking ranking = new Ranking();
        ranking.setName(rank.toString());
        ranking.setId(1);
        return ranking;
    }
}
