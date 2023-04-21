package com.telerikacademy.web.photocontest.services.contracts;

import com.telerikacademy.web.photocontest.models.User;
import org.springframework.ui.Model;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public interface EmailServices {

    void sendForgottenPasswordEmail(User recipient, HttpSession session) throws MessagingException, IOException;
    boolean isDeliverable(String email) throws IOException;
}
