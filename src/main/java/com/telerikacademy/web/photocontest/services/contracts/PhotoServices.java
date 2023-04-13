package com.telerikacademy.web.photocontest.services.contracts;

import com.telerikacademy.web.photocontest.models.Photo;
import com.telerikacademy.web.photocontest.models.PhotoScore;
import com.telerikacademy.web.photocontest.models.PhotoReviewDetails;
import com.telerikacademy.web.photocontest.models.User;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhotoServices {
    List<Photo> getAll();
    void create(Photo photo, MultipartFile file) throws FileUploadException;
    void delete(Photo photo, User user);
    Photo getById(Long id);
    void postReview(PhotoScore photoScore, Photo photo, User user, PhotoReviewDetails photoReviewDetails);
    int getScoreOfPhoto(Long id);
}
