package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.models.*;
import com.telerikacademy.web.photocontest.models.dto.*;
import com.telerikacademy.web.photocontest.services.contracts.CategoryServices;
import com.telerikacademy.web.photocontest.services.contracts.ContestServices;
import com.telerikacademy.web.photocontest.services.contracts.RankingServices;
import com.telerikacademy.web.photocontest.services.contracts.UserServices;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

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

    public ContestResponseDto objectToDto(Contest contest) {
        List<String> juries = null ;
        if (contest.getJuries() != null) juries = contest.getJuries().stream().map(User::getUsername).toList();
        List<String> participants = null;
        if (contest.getParticipants() != null) participants = contest.getParticipants().stream().map(User::getUsername).toList();
        List<Photo> photos = null;
        if (contest.getPhotos() != null) photos = contest.getPhotos().stream().toList();

        return new ContestResponseDto(contest.getId(), contest.getTitle(), contest.getCategory().getName(), contest.getPhase1(), contest.getPhase2(),
                contest.getDateCreated(), contest.getCoverPhoto(), contest.isInvitational(), juries, participants, photos);
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

    public Photo dtoToObject(PhotoDto photoDto) {
        Photo photo = new Photo();
        photo.setTitle(photoDto.getTitle());
        photo.setStory(photoDto.getStory());
        photo.setPostedOn(contestServices.findById(photoDto.getContestId()));
        photo.setPhoto(photoDto.getPhoto());

        return photo;
    }

    public PhotoReview dtoToObject(PhotoReviewDto photoReviewDto) {
        PhotoReview photoReview = new PhotoReview();
        photoReview.setScore(photoReviewDto.isFitsCategory() ? photoReviewDto.getScore() : 0);
        photoReview.setComment(photoReviewDto.getComment());

        return photoReview;
    }
}
