package com.telerikacademy.web.photocontest.services.contracts;

import com.telerikacademy.web.photocontest.models.Ranking;

public interface RankingServices {
    Ranking getJunkie();
    Ranking getEnthusiast();
    Ranking getMaster();
    Ranking getWiseAndBenevolentPhotoDictator();

}
