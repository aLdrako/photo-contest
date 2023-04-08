package com.telerikacademy.web.photocontest.models;

public enum Ranks {
    JUNKIE("Junkie"),
    MASTER("Master"),
    ENTHUSIAST("Enthusiast"),
    WISE_AND_BENEVOLENT_PHOTO_DICTATOR("Wise and Benevolent Photo Dictator");

    private final String value;
    Ranks(String value) {
        this.value = value;
    }
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
