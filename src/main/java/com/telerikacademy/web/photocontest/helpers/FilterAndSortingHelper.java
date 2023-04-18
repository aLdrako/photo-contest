package com.telerikacademy.web.photocontest.helpers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Map;

public class FilterAndSortingHelper {
    public record Result(String title, String categoryName, Boolean isInvitational, Boolean isFinished, LocalDateTime phase1, LocalDateTime phase2, Pageable pageable) {
    }

    public static Result getResult(Map<String, String> parameter, Pageable pageable) {
        String title = parameter.get("title");
        if (title != null && title.isEmpty()) title = null;
        String categoryName = parameter.get("category");
        if (categoryName != null && categoryName.isEmpty()) categoryName = null;
        Boolean isInvitational = null;
        String type = parameter.get("type");
        if (type != null && !type.equals("all")) isInvitational = Boolean.parseBoolean(type);
        Boolean isFinished = parameter.get("finished") != null ? Boolean.parseBoolean(parameter.get("finished")) : null;
        LocalDateTime phase1 = (parameter.get("phase1") != null && !parameter.get("phase1").isEmpty()) ? LocalDateTime.parse(parameter.get("phase1")) : null;
        LocalDateTime phase2 = (parameter.get("phase2") != null && !parameter.get("phase2").isEmpty()) ? LocalDateTime.parse(parameter.get("phase2")) : null;
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        return new Result(title, categoryName, isInvitational, isFinished, phase1, phase2, pageRequest);
    }
}
