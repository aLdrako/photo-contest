package com.telerikacademy.web.photocontest.helpers;

import java.util.Random;

public class RandomStringGenerator {
    public static String generateString() {
        return new Random().ints(48, 123)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)).limit(15)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }
}
