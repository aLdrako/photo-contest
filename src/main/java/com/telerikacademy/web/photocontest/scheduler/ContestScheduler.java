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

    private void calculateScore(Set<ContestResults> results) {
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

    public static Map<Integer, Integer> awardPoints(List<Integer> scores) {

        List<Integer> sortedScores = new ArrayList<>(scores);
        Collections.sort(sortedScores);
        int n = sortedScores.size();

        Map<Integer, Integer> points = new HashMap<>();
        int place = 1;
        int[] places = {0, 0, 0};

        for (int i = n - 1; i >= 0; i--) {
            if (i < n - 1 && !Objects.equals(sortedScores.get(i), sortedScores.get(i + 1))) {
                place++;
            }

            if (place <= 3) {
                places[place - 1]++;
            }
        }

        for (int i = 0; i < scores.size(); i++) {
            int score = scores.get(i);
            int point = 0;

            if (score == sortedScores.get(n - 1)) {
                point = places[0] > 1 ? 40 : 50;
            } else if (score == sortedScores.get(n - places[0] - 1)) {
                point = places[1] > 1 ? 25 : 35;
            } else if (places[2] > 0 && score == sortedScores.get(n - places[0] - places[1] - 1)) {
                point = places[2] > 1 ? 10 : 20;
            }

            points.put(i, point);
        }

        return points;
    }
}
