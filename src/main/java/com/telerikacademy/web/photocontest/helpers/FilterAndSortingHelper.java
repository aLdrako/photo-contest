package com.telerikacademy.web.photocontest.helpers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Map;

public class FilterAndSortingHelper {
    public record Result(String title, String categoryName, Boolean isInvitational, Boolean isFinished, String phase, LocalDateTime now, Pageable pageable) {
    }

    public static Result getResult(Map<String, String> parameter, Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        String title = parameter.get("title");
        if (title != null && title.isEmpty()) title = null;
        String categoryName = parameter.get("category");
        if (categoryName != null && categoryName.isEmpty()) categoryName = null;
        Boolean isInvitational = null;
        String type = parameter.get("type");
        if (type != null && !type.equals("all")) isInvitational = Boolean.parseBoolean(type);
        Boolean isFinished = null;
        String phase = parameter.get("phase");
        if (phase != null && phase.equals("all")) {
            phase = null;
        } else if (phase != null && phase.equals("finished")) {
            isFinished = true;
            phase = null;
        }
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        return new Result(title, categoryName, isInvitational, isFinished, phase, now, pageRequest);
    }
}
