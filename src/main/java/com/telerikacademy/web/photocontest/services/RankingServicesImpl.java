package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.models.Ranking;
import com.telerikacademy.web.photocontest.models.Ranks;
import com.telerikacademy.web.photocontest.repositories.contracts.RankingRepository;
import com.telerikacademy.web.photocontest.services.contracts.RankingServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RankingServicesImpl implements RankingServices {

    private final RankingRepository rankingRepository;
    @Override
    public Ranking getJunkie() {
        return returnRanking(Ranks.JUNKIE);
    }

    @Override
    public Ranking getEnthusiast() {
        return returnRanking(Ranks.ENTHUSIAST);
    }

    @Override
    public Ranking getMaster() {
        return returnRanking(Ranks.MASTER);
    }

    @Override
    public Ranking getWiseAndBenevolentPhotoDictator() {
        return returnRanking(Ranks.WISE_AND_BENEVOLENT_PHOTO_DICTATOR);
    }
    private Ranking returnRanking(Ranks ranking) {
        try {
            return rankingRepository.getByName(ranking.name());
        } catch (EntityNotFoundException e) {
            Ranking newRanking = new Ranking(ranking.name());
            rankingRepository.create(newRanking);
            return newRanking;
        }
    }
}
