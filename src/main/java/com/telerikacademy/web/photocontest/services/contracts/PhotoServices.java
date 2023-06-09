package com.telerikacademy.web.photocontest.services.contracts;

import com.telerikacademy.web.photocontest.models.*;
import com.telerikacademy.web.photocontest.models.dto.PhotoResponseDto;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PhotoServices {
    List<Photo> getAll();
    void create(Photo photo, MultipartFile file) throws FileUploadException;
    void delete(Photo photo, User user);
    Photo getById(Long id);
    void postReview(PhotoScore photoScore, Photo photo, User user, PhotoReviewDetails photoReviewDetails);
    int getScoreOfPhoto(Long id);
    Photo getPhotoByContestId(Long photoId, Long contestId);

    List<Photo> getPhotosOfContest(Contest contest);

    Page<Photo> search(String title, Long contestId, Long userId, Pageable pageable);

    void alreadyUploadedPhoto(Contest contest, User user);
}
