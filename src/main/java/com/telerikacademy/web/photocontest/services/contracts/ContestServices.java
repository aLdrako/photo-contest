package com.telerikacademy.web.photocontest.services.contracts;

import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.models.User;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public interface ContestServices {
    Iterable<Contest> findAll();
    Contest findById(Long id);
    Contest create(Contest contest, User authenticatedUser, MultipartFile coverPhotoUpload) throws FileUploadException;
    Contest update(Contest contest, User authenticatedUser, MultipartFile coverPhotoUpload) throws FileUploadException;
    Contest uploadCover(Contest contest, User authenticatedUser);
    void deleteById(Long id, User authenticatedUser);
    Page<Contest> filter(String title, String categoryName, Boolean isInvitational, Boolean isFinished, String phase, LocalDateTime now, Pageable pageable);
    Contest join(Contest contest, User authenticatedUser);
    Contest addJury(Contest contest, User authenticatedUser, String username);
    Contest addParticipant(Contest contest, User authenticatedUser, String username);
    void checkUniqueness(String title);
    void evaluateRank(User user);
}
