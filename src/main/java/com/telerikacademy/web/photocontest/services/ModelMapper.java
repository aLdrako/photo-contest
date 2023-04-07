package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.models.dto.ContestDto;
import com.telerikacademy.web.photocontest.services.contracts.CategoryServices;
import com.telerikacademy.web.photocontest.services.contracts.ContestServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class ModelMapper {
    private final ContestServices contestServices;
    private final CategoryServices categoryServices;

    // TODO to implement cover photo
    public Contest dtoToObject(ContestDto contestDto) {
        Contest contest = new Contest();
        contest.setTitle(contestDto.getTitle());
        contest.setCategory(categoryServices.findById(contestDto.getCategoryId()).orElseThrow(() -> new EntityNotFoundException("Category")));
        contest.setInvitational(contestDto.isInvitational());
        contest.setPhase1(contestDto.getPhase1());
        contest.setPhase2(contestDto.getPhase2());
        return contest;
    }

    public Contest dtoToObject(Long id, ContestDto contestDto) {
        Contest contest = contestServices.findById(id).orElseThrow(() -> new EntityNotFoundException("Contest", id));
        if (contestDto.getTitle() != null) contest.setTitle(contestDto.getTitle());
        if (contestDto.getCategoryId() != null) contest.setCategory(categoryServices.findById(contestDto.getCategoryId()).orElseThrow(() -> new EntityNotFoundException("Category", id)));
        if (contestDto.getPhase1() != null) contest.setPhase1(contestDto.getPhase1());
        if (contestDto.getPhase2() != null) contest.setPhase1(contestDto.getPhase2());
        contest.setInvitational(contestDto.isInvitational());
        return contest;
    }
}
