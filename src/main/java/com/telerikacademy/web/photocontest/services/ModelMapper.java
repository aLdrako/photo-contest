package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.models.Category;
import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.models.dto.CategoryDto;
import com.telerikacademy.web.photocontest.models.dto.ContestDto;
import com.telerikacademy.web.photocontest.models.dto.ContestOutputDto;
import com.telerikacademy.web.photocontest.models.dto.UserDto;
import com.telerikacademy.web.photocontest.services.contracts.CategoryServices;
import com.telerikacademy.web.photocontest.services.contracts.ContestServices;
import com.telerikacademy.web.photocontest.services.contracts.RankingServices;
import com.telerikacademy.web.photocontest.services.contracts.UserServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
@AllArgsConstructor
public class ModelMapper {
    private final ContestServices contestServices;
    private final CategoryServices categoryServices;
    private final RankingServices rankingServices;
    private final UserServices userServices;

    public Contest dtoToObject(ContestDto contestDto) {
        Contest contest = new Contest();
        contest.setTitle(contestDto.getTitle());
        contest.setCategory(categoryServices.findById(contestDto.getCategoryId()));
        contest.setPhase1(contestDto.getPhase1());
        contest.setPhase2(contestDto.getPhase2());
        contest.setInvitational(contestDto.isInvitational());
//        if (contestDto.isInvitational()) contest.setParticipants();
        contest.setJuries(new HashSet<>(userServices.getAllOrganizers()));
        contest.setCoverPhoto(contestDto.getPhoto());
        return contest;
    }

    public Contest dtoToObject(Long id, ContestDto contestDto) {
        Contest contest = contestServices.findById(id);
        if (contestDto.getCategoryId() != null) contest.setCategory(categoryServices.findById(contestDto.getCategoryId()));
        if (contestDto.getTitle() != null) contest.setTitle(contestDto.getTitle());
        return contest;
    }

    public ContestOutputDto objectToDto(Contest contest) {
        ContestOutputDto contestOutputDto = new ContestOutputDto();
        contestOutputDto.setId(contest.getId());
        contestOutputDto.setTitle(contest.getTitle());
        contestOutputDto.setCategory(contest.getCategory().getName());
        contestOutputDto.setPhase1(contest.getPhase1());
        contestOutputDto.setPhase2(contest.getPhase2());
        contestOutputDto.setDateCreated(contest.getDateCreated());
        contestOutputDto.setCoverPhoto(contest.getCoverPhoto());
        contestOutputDto.setInvitational(contest.isInvitational());
        if (contest.getJuries() != null) contestOutputDto.setJuries(contest.getJuries().stream().map(User::getUsername).toList());
        if (contest.getParticipants() != null) contestOutputDto.setParticipants(contest.getParticipants().stream().map(User::getUsername).toList());
        if (contest.getPhotos() != null) contestOutputDto.setPhotos(contest.getPhotos().stream().toList());
        return contestOutputDto;
    }

    public Category dtoToObject(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        return category;
    }

    public Category dtoToObject(Long id, CategoryDto categoryDto) {
        Category category = categoryServices.findById(id);
        if (categoryDto.getName() != null) category.setName(categoryDto.getName());
        return category;
    }

    public User dtoToObject(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setRank(rankingServices.getJunkie());
        return user;
    }

    public User dtoToObject(Long id, UserDto userDto) {
        User userFromRepo = userServices.getById(id);
        if (userDto.getFirstName() != null) userFromRepo.setFirstName(userDto.getFirstName());
        if (userDto.getLastName() != null) userFromRepo.setLastName(userDto.getLastName());
        if (userDto.getPassword() != null) userFromRepo.setPassword(userDto.getPassword());
        if (userDto.getEmail() != null) userFromRepo.setEmail(userDto.getEmail());

        return userFromRepo;
    }
}
