package com.telerikacademy.web.photocontest.helpers;

import com.telerikacademy.web.photocontest.models.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Set;

public class Helpers {


    public static Ranking createMockRanking(Ranks rank) {
        Ranking ranking = new Ranking();
        ranking.setName(rank.toString());
        ranking.setId(1);
        return ranking;
    }
    public static Photo createMockPhoto() {
        Photo photo = new Photo();
        photo.setUserCreated(createMockUser());
        photo.setStory("mockStory");
        photo.setTitle("mockTitle");
        photo.setId(1L);
        photo.setPhoto("mockPathName");
        photo.setPostedOn(createMockContest());
        return photo;
    }
    public static MultipartFile createMockFile() {
        return new MockMultipartFile("mockFileName", "mockOriginalFileName",
                "mockContentType.image/", new byte[5]);
    }

    public static Contest createMockContest() {
        Contest contest = new Contest();
        contest.setId(1L);
        contest.setTitle("mockTitle");
        contest.setCategory(createMockCategory());
        contest.setCoverPhoto("mockPhotoURL");
        contest.setPhase1(LocalDateTime.now());
        contest.setPhase2(LocalDateTime.now());
        contest.setDateCreated(LocalDateTime.now());
        contest.setJuries(Set.of());
        contest.setParticipants(Set.of());
        return contest;
    }

    public static Category createMockCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("mockCategory");
        return category;
    }

    public static User createMockUser() {
        User user = new User();
        user.setId(1L);
        user.setOrganizer(false);
        user.setFirstName("mockFirstName");
        user.setLastName("mockLastName");
        user.setUsername("mockUsername");
        user.setPassword("mockPassword");
        user.setEmail("mockEmail@email.com");
        user.setRank(createMockRanking(Ranks.JUNKIE));
        user.setJoinDate(LocalDateTime.now());
        return user;
    }
    public static User createMockOrganizer() {
        User user = createMockUser();
        user.setId(2L);
        user.setOrganizer(true);

        return user;
    }
}