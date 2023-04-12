package com.telerikacademy.web.photocontest.helpers;

import com.jcraft.jsch.*;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Random;

public class FileUploadHelper {
    public static String uploadPhoto(MultipartFile file) throws FileUploadException {
        if (file.isEmpty()) {
            throw new FileUploadException("Please upload an actual photo");
        }
        if (!Objects.requireNonNull(file.getContentType()).contains("image/")) {
            throw new FileUploadException("jpg/jpeg/png file types are only supported");
        }
        int lastIndex = Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf('.');
        String fileExtension = lastIndex != -1 ? file.getOriginalFilename().substring(lastIndex) : "";
        String fileName = generateString() + fileExtension;

        ftpConnection(file, fileName, false);

        return "https://alexgo.online/Projects/PhotoContest/photos/" + fileName;
    }

    public static void deletePhoto(String filePathName) {
        int lastIndex = filePathName.lastIndexOf('/');
        String fileName = lastIndex != -1 ? filePathName.substring(lastIndex + 1) : "";

        ftpConnection(null, fileName, true);
    }

    private static void ftpConnection(MultipartFile file, String fileName, boolean isDeletion) {
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession("u1471027597", "access946642142.webspace-data.io", 22);
            session.setPassword("soLid_Photo_Contest");
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();

            if (isDeletion) {
                channel.rm("/photos/" + fileName);
            } else {
                InputStream inputStream = file.getInputStream();
                channel.put(inputStream, "/photos/" + fileName);
                inputStream.close();
            }

            channel.disconnect();
            session.disconnect();
        } catch (IOException | JSchException | SftpException e) {
            e.getStackTrace();
        }
    }

    private static String generateString() {
        return new Random().ints(48, 123)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)).limit(15)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }
}
