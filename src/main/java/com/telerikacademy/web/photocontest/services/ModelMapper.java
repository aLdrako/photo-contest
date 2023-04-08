package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.models.dto.ContestDto;
import com.telerikacademy.web.photocontest.services.contracts.CategoryServices;
import com.telerikacademy.web.photocontest.services.contracts.ContestServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ModelMapper {
    private final ContestServices contestServices;
    private final CategoryServices categoryServices;

    public Contest dtoToObject(ContestDto contestDto) {
        Contest contest = new Contest();
        contest.setTitle(contestDto.getTitle());
        contest.setCategory(categoryServices.findById(contestDto.getCategoryId()));
        contest.setPhase1(contestDto.getPhase1());
        contest.setPhase2(contestDto.getPhase2());
        contest.setInvitational(contestDto.isInvitational());
        contest.setCoverPhoto(contestDto.getPhoto());
//        if (contestDto.isInvitational()) contest.setParticipants();
//        contest.setJuries();
        return contest;
    }

    public Contest dtoToObject(Long id, ContestDto contestDto) {
        Contest contest = contestServices.findById(id);
        if (contestDto.getCategoryId() != null) contest.setCategory(categoryServices.findById(contestDto.getCategoryId()));
        if (contestDto.getTitle() != null) contest.setTitle(contestDto.getTitle());
        return contest;
    }
}
