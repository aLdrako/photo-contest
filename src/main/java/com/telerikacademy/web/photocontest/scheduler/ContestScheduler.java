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

        List<Integer> resultList = new ArrayList<>(results.stream()
                .mapToInt(ContestResults::getResults)
                .sorted().boxed().toList());
        Collections.reverse(resultList);

        List<Integer> uniqueResults = results.stream()
                .map(ContestResults::getResults)
                .distinct().toList();

        int numResults = resultList.size();
        int numTies = 0;

        for (int i = 0; i < numResults; i++) {
            ContestResults contestResults = sortedResults.get(i);
            int resultInt = resultList.get(i);
            int points = Collections.frequency(resultList, resultInt);

            // Check for shared positions
            if (uniqueResults.contains(resultInt)) {
                numTies = Collections.frequency(resultList, resultInt);
            }

            // Calculate points based on position and ties
            if (i == 0) {
                // First place
                if (numTies > 1) {
                    points = 40 / numTies;
                } else {
                    if (numResults > 1 && resultInt >= 2 * resultInt) {
                        points = 75;
                    } else {
                        points = 50;
                    }
                }
            } else if (i == 1) {
                // Second place
                if (numTies > 1) {
                    points = 25 / numTies;
                } else {
                    points = 35;
                }
            } else if (i == 2) {
                // Third place
                if (numTies > 1) {
                    points = 10 / numTies;
                } else {
                    points = 20;
                }
            } else {
                // Other positions
                if (numTies > 1) {
                    points = 5 / numTies;
                } else {
                    points = 10;
                }
            }

            User user = contestResults.getResultEmbed().getPhoto().getUserCreated();
            user.setPoints(user.getPoints() + points);
            contestServices.evaluateRank(user);
        }
    }
}
