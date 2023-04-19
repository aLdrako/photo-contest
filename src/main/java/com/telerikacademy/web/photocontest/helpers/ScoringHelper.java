package com.telerikacademy.web.photocontest.helpers;

import java.util.*;

public class ScoringHelper {

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
