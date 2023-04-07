package com.telerikacademy.web.photocontest.repositories.contracts;

import com.telerikacademy.web.photocontest.models.Contest;

import java.util.List;

public interface ContestRepository {
    List<Contest> get();
}
