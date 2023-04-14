package com.telerikacademy.web.photocontest.helpers;

import java.time.LocalDateTime;
import java.util.Map;

public class FilterAndSortingHelper {
    public record Result(String title, String categoryName, Boolean isInvitational, Boolean isFinished, LocalDateTime phase1, LocalDateTime phase2) {
    }

    public static Result getResult(Map<String, String> parameter) {
        String title = parameter.get("title");
        String categoryName = parameter.get("category");
        Boolean isInvitational = parameter.get("invitational") != null ? Boolean.parseBoolean(parameter.get("invitational")) : null;
        Boolean isFinished = parameter.get("finished") != null ? Boolean.parseBoolean(parameter.get("finished")) : null;
        LocalDateTime phase1 = parameter.get("phase1") != null ? LocalDateTime.parse(parameter.get("phase1")) : null;
        LocalDateTime phase2 = parameter.get("phase2") != null ? LocalDateTime.parse(parameter.get("phase2")) : null;
        return new Result(title, categoryName, isInvitational, isFinished, phase1, phase2);
    }
}
