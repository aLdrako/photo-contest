package com.telerikacademy.web.photocontest.helpers;

import java.util.Map;

public class FilterAndSortingHelper {
    public record Result(String title, String categoryName, Boolean isInvitational) {
    }

    public static Result getResult(Map<String, String> parameter) {
        String title = parameter.get("title");
        String categoryName = parameter.get("category");
        Boolean isInvitational = parameter.get("type") != null ? Boolean.parseBoolean(parameter.get("type")) : null;
        return new Result(title, categoryName, isInvitational);
    }
}
