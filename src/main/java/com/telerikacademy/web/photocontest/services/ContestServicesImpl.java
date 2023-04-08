package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.exceptions.EntityDuplicateException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
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

    public Iterable<Contest> findAll() {
        return contestRepository.findAll();
    }

    public Contest findById(Long id) {
        return contestRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Contest", id));
    }

    public Contest save(Contest contest) {
        checkUniqueness(contest);
        return contestRepository.save(contest);
    }

    public void deleteById(Long id) {
        Contest contestToDelete = findById(id);
        contestRepository.deleteById(id);
    }

    @Override
    public List<Contest> filter(String title, String categoryName, Boolean type) {
        return contestRepository.filter(title, categoryName, type);
    }

    private void checkUniqueness(Contest contest) {
        if (contestRepository.existsByTitleEqualsIgnoreCase(contest.getTitle())) {
            throw new EntityDuplicateException("Contest", "title", contest.getTitle());
        }
    }
}
