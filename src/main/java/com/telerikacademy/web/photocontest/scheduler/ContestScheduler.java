package com.telerikacademy.web.photocontest.scheduler;

import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.models.ContestResults;
import com.telerikacademy.web.photocontest.models.PhotoScore;
import com.telerikacademy.web.photocontest.models.ResultId;
import com.telerikacademy.web.photocontest.repositories.contracts.ContestRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

@Component
@EnableScheduling
@AllArgsConstructor
@Slf4j
public class ContestScheduler {

    private final ContestRepository contestRepository;

    @Scheduled(fixedDelay = 300000) // 5 min update
    public void checkContestPhases() {
        List<Contest> contests = contestRepository.findByPhase2IsBeforeAndIsFinishedFalse(LocalDateTime.now());
        for (Contest contest : contests) {
            Set<ContestResults> results = new HashSet<>();
            contest.getPhotos().forEach(photo -> {
                ContestResults result = new ContestResults();
                result.setResultId(new ResultId(contest, photo));
                result.setResults(photo.getScores().stream().mapToInt(PhotoScore::getScore).sum());
                results.add(result);
            });
            contest.setResults(results);
            contest.setIsFinished(true);
            contestRepository.save(contest);
        }
    }
}
