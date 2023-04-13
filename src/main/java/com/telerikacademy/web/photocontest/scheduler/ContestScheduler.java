package com.telerikacademy.web.photocontest.scheduler;

import com.telerikacademy.web.photocontest.models.*;
import com.telerikacademy.web.photocontest.repositories.contracts.ContestRepository;
import com.telerikacademy.web.photocontest.services.contracts.ContestServices;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@EnableScheduling
@AllArgsConstructor
@Slf4j
public class ContestScheduler {

    private final ContestRepository contestRepository;
    private final ContestServices contestServices;

    @Scheduled(fixedDelay = 300000) // 5 min update
    public void checkContestPhases() {
        List<Contest> contests = contestRepository.findByPhase2IsBeforeAndIsFinishedFalse(LocalDateTime.now());
        for (Contest contest : contests) {
            Set<ContestResults> results = new HashSet<>();
            contest.getPhotos().forEach(photo -> {
                int totalScore = photo.getScores().stream().mapToInt(PhotoScore::getScore).sum();

                ContestResults result = new ContestResults();
                result.setResultEmbed(new ResultEmbed(contest, photo));
                result.setResults(totalScore);

                results.add(result);
            });

            calculateScore(results);

            contest.setResults(results);
            contest.setIsFinished(true);
            contestRepository.save(contest);
        }
    }

    // TODO minor fix in calculations
    private void calculateScore(Set<ContestResults> results) {
        List<ContestResults> sortedResults = results.stream()
                .sorted(Comparator.comparing(ContestResults::getResults).reversed()).toList();

        Set<Integer> uniqueResults = results.stream()
                .map(ContestResults::getResults).collect(Collectors.toSet());

        int numResults = sortedResults.size();

        for (int i = 0; i < numResults; i++) {
            ContestResults result = sortedResults.get(i);
            int score = result.getResults();
            int points = 0;
            int numTies = 0;

            if (uniqueResults.contains(score)) {
                numTies = Collections.frequency(sortedResults, result);
            }

            if (i == 0 && numTies == 0) {
                points = 50;

                if (numResults > 1 && score >= 2 * sortedResults.get(1).getResults()) {
                    points = 75;
                }
            } else if (i == 0 && numTies > 0) {
                points = 40 / numTies;

                if (numResults > 1 && score >= 2 * sortedResults.get(1).getResults()) {
                    points = 60 / numTies;
                }
            } else if (i == 1 && numTies == 0) {
                points = 35;
            } else if (i == 1 && numTies > 0) {
                points = 25 / numTies;
            } else if (i == 2 && numTies == 0) {
                points = 20;
            } else if (i == 2 && numTies > 0) {
                points = 10 / numTies;
            }

            User user = result.getResultEmbed().getPhoto().getUserCreated();
            user.setPoints(user.getPoints() + points);
            contestServices.evaluateRank(user);
        }
    }
}
