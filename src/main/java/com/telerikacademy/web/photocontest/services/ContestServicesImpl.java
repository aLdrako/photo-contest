package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.repositories.contracts.ContestRepository;
import com.telerikacademy.web.photocontest.services.contracts.ContestServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ContestServicesImpl implements ContestServices {

    private final ContestRepository contestRepository;

    public Iterable<Contest> findAll() {
        return contestRepository.findAll();
    }

    public Optional<Contest> findById(Long id) {
        return contestRepository.findById(id);
    }

    public Contest save(Contest contest) {
        return contestRepository.save(contest);
    }

    public void deleteById(Long id) {
        contestRepository.deleteById(id);
    }
}
