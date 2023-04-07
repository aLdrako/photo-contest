package com.telerikacademy.web.photocontest.models;

public enum Ranks {
    JUNKIE, MASTER, ENTHUSIAST, WISE_AND_BENEVOLENT_PHOTO_DICTATOR;

    @Override
    public String toString() {
        return switch (this) {
            case JUNKIE -> "Junkie";
            case MASTER -> "Master";
            case ENTHUSIAST -> "Enthusiast";
            case WISE_AND_BENEVOLENT_PHOTO_DICTATOR -> "Wise and Benevolent Photo Dictator";
        };
    }
}
