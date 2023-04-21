package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.models.*;
import com.telerikacademy.web.photocontest.models.dto.*;
import com.telerikacademy.web.photocontest.services.contracts.CategoryServices;
import com.telerikacademy.web.photocontest.services.contracts.ContestServices;
import com.telerikacademy.web.photocontest.services.contracts.RankingServices;
import com.telerikacademy.web.photocontest.services.contracts.UserServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.telerikacademy.web.photocontest.helpers.FileUploadHelper.deletePhoto;

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
        if (!contest.getTitle().equals(contestDto.getTitle())) {
            contestServices.checkUniqueness(contestDto.getTitle());
        }
        if (contestDto.getCategoryId() != null) contest.setCategory(categoryServices.findById(contestDto.getCategoryId()));
        if (contestDto.getTitle() != null) contest.setTitle(contestDto.getTitle());
        if (!contestDto.getCoverPhoto().isEmpty()) {
            deletePhoto(contest.getCoverPhoto());
            contest.setCoverPhoto(contestDto.getCoverPhoto());
        }
        return contest;
    }

    public ContestDto objectToDto(Long id) {
        Contest contest = contestServices.findById(id);
        ContestDto contestDto = new ContestDto();
        contestDto.setTitle(contest.getTitle());
        contestDto.setCategoryId(contest.getCategory().getId());
        return contestDto;
    }

    public ContestResponseDto objectToDto(Contest contest) {
        List<String> juries = null ;
        if (contest.getJuries() != null) juries = contest.getJuries().stream().map(User::getUsername).toList();
        List<String> participants = null;
        if (contest.getParticipants() != null) participants = contest.getParticipants().stream().map(User::getUsername).toList();
        List<Map<String, Object>> photos = getPhotos(contest);
        List<ContestResultDto> results = getContestResultDto(contest);
        List<Map<String, Object>> winners = getWinners(contest);

        return new ContestResponseDto(contest.getId(), contest.getTitle(), contest.getCategory().getName(), contest.getPhase1(), contest.getPhase2(),
                contest.getDateCreated(), contest.getCoverPhoto(), contest.isInvitational(), contest.getIsFinished(), juries, participants, photos, results, winners);
    }

    public UserDto objectToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setPasswordConfirm(user.getPassword());
        userDto.setOrganizer(user.isOrganizer());
        return userDto;
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
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty())
            userFromRepo.setPassword(userDto.getPassword());
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
    public PhotoReviewResponseDto objectToDto(PhotoReviewDetails photoReviewDetails) {
        return new PhotoReviewResponseDto(photoReviewDetails.getReviewId().getPhotoId().getId(),
                photoReviewDetails.getReviewId().getJuryId().getUsername(), photoReviewDetails.getComment(),
                photoReviewDetails.getPhotoScore().getScore());
    }
    public PhotoResponseDto objectToDto(Photo photo) {
        return new PhotoResponseDto(photo.getTitle(), photo.getStory(), photo.getPhoto(),
                photo.getUserCreated().getUsername(), photo.getPostedOn().getId());
    }

    private static List<ContestResultDto> getContestResultDto(Contest contest) {
        List<ContestResultDto> results = null;
        if (contest.getResults() != null) {
            results = contest.getResults().stream()
                    .map(contestResults -> {
                        Long id = contestResults.getResultEmbed().getPhoto().getId();
                        String photo = contestResults.getResultEmbed().getPhoto().getPhoto();
                        Long userId = contestResults.getResultEmbed().getPhoto().getUserCreated().getId();
                        String userCreated = contestResults.getResultEmbed().getPhoto().getUserCreated().getUsername();
                        int totalScore = contestResults.getResults();

                        Map<Long, Map<String, Integer>> juryScores = new HashMap<>();
                        contestResults.getResultEmbed().getPhoto().getReviewsDetails().forEach(photoReviewDetails -> {
                            Long photoId = photoReviewDetails.getReviewId().getPhotoId().getId();
                            String jury = photoReviewDetails.getReviewId().getJuryId().getUsername();
                            int score = photoReviewDetails.getReviewId().getPhotoId().getScores()
                                    .stream()
                                    .filter(photoScore -> photoScore.getReviewId().getJuryId().getUsername().equals(jury))
                                    .findFirst()
                                    .map(PhotoScore::getScore)
                                    .orElse(0);

                            Map<String, Integer> scores = juryScores.computeIfAbsent(photoId, k -> new HashMap<>());
                            scores.put(jury, score);
                        });

                        List<Map<String, Object>> juriesMap = new ArrayList<>();
                        contestResults.getResultEmbed().getPhoto().getReviewsDetails().forEach(photoReviewDetails -> {
                            Long photoId = photoReviewDetails.getReviewId().getPhotoId().getId();
                            String jury = photoReviewDetails.getReviewId().getJuryId().getUsername();
                            int score = juryScores.get(photoId).getOrDefault(jury, 3);
                            String comment = photoReviewDetails.getComment();

                            Map<String, Object> scoreDetails = new HashMap<>();
                            scoreDetails.put("jury", jury);
                            scoreDetails.put("score", score);
                            scoreDetails.put("comment", comment);
                            juriesMap.add(scoreDetails);
                        });

                        return new ContestResultDto(id, photo, userId, userCreated, juriesMap, totalScore);
                    }).toList();
        }
        return results;
    }

    private static List<Map<String, Object>> getPhotos(Contest contest) {
        List<Map<String, Object>> photos = null;
        if (contest.getPhotos() != null) {
            photos = contest.getPhotos().stream().map(photo -> {
                Map<String, Object> photoMap = new HashMap<>();
                photoMap.put("id", photo.getId());
                photoMap.put("title", photo.getTitle());
                photoMap.put("story", photo.getStory());
                photoMap.put("photo", photo.getPhoto());
                photoMap.put("userId", photo.getUserCreated().getId());
                photoMap.put("userCreated", photo.getUserCreated().getUsername());
                return photoMap;
            }).toList();
        }
        return photos;
    }

    private static List<Map<String, Object>> getWinners(Contest contest) {
        int maxScore = contest.getResults().stream()
                .mapToInt(ContestResults::getResults)
                .max()
                .orElse(0);
        return contest.getResults().stream()
                .filter(result -> result.getResults() == maxScore)
                .map(contestResults -> {
                    Map<String, Object> win = new HashMap<>();
                    win.put("photo", contestResults.getResultEmbed().getPhoto().getPhoto());
                    win.put("userCreated", contestResults.getResultEmbed().getPhoto().getUserCreated().getUsername());
                    win.put("totalScore", contestResults.getResults());
                    return win;
                }).toList();
    }
}
