package com.telerikacademy.web.photocontest.scheduler;

import com.telerikacademy.web.photocontest.models.*;
import com.telerikacademy.web.photocontest.repositories.contracts.ContestRepository;
import com.telerikacademy.web.photocontest.services.contracts.ContestServices;
import com.telerikacademy.web.photocontest.services.contracts.PhotoServices;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

import static com.telerikacademy.web.photocontest.helpers.ScoringHelper.awardPoints;

@Component
@EnableScheduling
@AllArgsConstructor
@Slf4j
public class ContestScheduler {

    private final ContestRepository contestRepository;
    private final ContestServices contestServices;
    private final PhotoServices photoServices;

    @Scheduled(fixedDelay = 300000) // 5 min update
    public void checkContestPhases() {
        List<Contest> contests = contestRepository.findByPhase2IsBeforeAndIsFinishedFalse(LocalDateTime.now());
        for (Contest contest : contests) {
            Set<ContestResults> results = new HashSet<>();
            contest.getPhotos().forEach(photo -> {
                int totalScore = photoServices.getScoreOfPhoto(photo.getId());
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

    public void calculateScore(Set<ContestResults> results) {
        List<ContestResults> sortedResults = results.stream()
                .sorted(Comparator.comparing(ContestResults::getResults).reversed()).toList();

        List<Integer> resultList = new ArrayList<>(results.stream()
                .mapToInt(ContestResults::getResults)
                .sorted().boxed().toList());
        Collections.reverse(resultList);

        Map<Integer, Integer> pointsList = awardPoints(resultList);

        for (int i = 0; i < resultList.size(); i++) {
            ContestResults contestResults = sortedResults.get(i);

            User user = contestResults.getResultEmbed().getPhoto().getUserCreated();
            user.setPoints(user.getPoints() + pointsList.get(i));
            contestServices.evaluateRank(user);
        }
    }
}
