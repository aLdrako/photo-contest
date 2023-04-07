package com.telerikacademy.web.photocontest.services.contracts;

import com.telerikacademy.web.photocontest.models.Contest;

import java.util.List;
import java.util.Optional;

public interface ContestServices {
    Iterable<Contest> findAll();
    Optional<Contest> findById(Long id);
    Contest save(Contest contest);
    void deleteById(Long id);
}
