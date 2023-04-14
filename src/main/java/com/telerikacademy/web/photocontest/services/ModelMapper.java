package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.models.*;
import com.telerikacademy.web.photocontest.models.dto.*;
import com.telerikacademy.web.photocontest.services.contracts.CategoryServices;
import com.telerikacademy.web.photocontest.services.contracts.ContestServices;
import com.telerikacademy.web.photocontest.services.contracts.RankingServices;
import com.telerikacademy.web.photocontest.services.contracts.UserServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
        if (contestDto.getJuries() != null) contest.setJuries(new HashSet<>(contestDto.getJuries().stream().map(userServices::getByUsername).toList()));
        contest.setCoverPhoto(contestDto.getCoverPhoto());
        return contest;
    }

    public Contest dtoToObject(Long id, ContestDto contestDto) {
        Contest contest = contestServices.findById(id);
        if (!contest.getTitle().equals(contestDto.getTitle())) contestServices.checkUniqueness(contestDto.getTitle());
        if (contestDto.getCategoryId() != null) contest.setCategory(categoryServices.findById(contestDto.getCategoryId()));
        if (contestDto.getTitle() != null) contest.setTitle(contestDto.getTitle());
        return contest;
    }

    public ContestResponseDto objectToDto(Contest contest) {
        List<String> juries = null ;
        if (contest.getJuries() != null) juries = contest.getJuries().stream().map(User::getUsername).toList();
        List<String> participants = null;
        if (contest.getParticipants() != null) participants = contest.getParticipants().stream().map(User::getUsername).toList();
        List<Map<String, Object>> photos = null;
        if (contest.getPhotos() != null) {
            photos = contest.getPhotos().stream().map(photo -> {
                Map<String, Object> photoMap = new HashMap<>();
                photoMap.put("id", photo.getId());
                photoMap.put("title", photo.getTitle());
                photoMap.put("story", photo.getStory());
                photoMap.put("photo", photo.getPhoto());
                photoMap.put("userCreated", photo.getUserCreated().getUsername());
                return photoMap;
            }).toList();
        }

        List<ContestResultDto> results = null;
        if (contest.getResults() != null) {
            results = contest.getResults().stream()
                    .map(contestResults -> new ContestResultDto(
                            contestResults.getResultEmbed().getPhoto().getId(),
                            contestResults.getResultEmbed().getPhoto().getPhoto(),
                            contestResults.getResultEmbed().getPhoto().getUserCreated().getUsername(),
                            contestResults.getResultEmbed().getPhoto().getReviewsDetails().stream()
                                    .map(photoReviewDetails -> photoReviewDetails.getReviewId().getJuryId().getUsername()).toList(),
                            contestResults.getResultEmbed().getPhoto().getReviewsDetails().stream()
                                    .map(PhotoReviewDetails::getComment).toList(),
                            contestResults.getResultEmbed().getPhoto().getScores().stream()
                                    .mapToInt(PhotoScore::getScore).boxed().toList(),
                            contestResults.getResults()
                    )).toList();
        }

        return new ContestResponseDto(contest.getId(), contest.getTitle(), contest.getCategory().getName(), contest.getPhase1(), contest.getPhase2(),
                contest.getDateCreated(), contest.getCoverPhoto(), contest.isInvitational(), contest.getIsFinished(), juries, participants, photos, results);
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

        return photo;
    }

    public PhotoScore dtoToObject(PhotoReviewDto photoReviewDto) {
        PhotoScore photoScore = new PhotoScore();
        photoScore.setScore(photoReviewDto.isFitsCategory() ? photoReviewDto.getScore() : 0);

        return photoScore;
    }
    public PhotoReviewDetails dtoToReviewDetails(PhotoReviewDto photoReviewDto) {
        PhotoReviewDetails photoReview = new PhotoReviewDetails();
        photoReview.setFitsCategory(photoReviewDto.isFitsCategory());
        photoReview.setComment(photoReviewDto.getComment());

        return photoReview;
    }
    public PhotoReviewResponseDto objectToDto(PhotoReviewDetails photoReviewDetails, PhotoScore score) {
        return new PhotoReviewResponseDto(photoReviewDetails.getReviewId().getPhotoId().getId(),
                photoReviewDetails.getReviewId().getJuryId().getId(), photoReviewDetails.getComment(),
                score.getScore());
    }
    public PhotoResponseDto objectToDto(Photo photo) {
        return new PhotoResponseDto(photo.getTitle(), photo.getStory(), photo.getPhoto(),
                photo.getUserCreated().getId(), photo.getPostedOn().getId());
    }
}
