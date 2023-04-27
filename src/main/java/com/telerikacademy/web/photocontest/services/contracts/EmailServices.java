package com.telerikacademy.web.photocontest.services.contracts;

import com.telerikacademy.web.photocontest.models.User;
import org.springframework.ui.Model;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

public interface EmailServices {

    void sendForgottenPasswordEmail(User recipient) throws MessagingException, IOException;
    Map<String, User> getUrlKeys();
    void clearKey(String urlKey);
}
