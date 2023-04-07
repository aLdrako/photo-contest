package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.repositories.contracts.ContestRepository;
import com.telerikacademy.web.photocontest.services.contracts.ContestServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ContestServicesImpl implements ContestServices {

    private final ContestRepository contestRepository;

    @Override
    public List<Contest> get() {
        return contestRepository.get();
    }
}
