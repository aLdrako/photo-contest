package com.telerikacademy.web.photocontest.services.contracts;

import com.telerikacademy.web.photocontest.models.User;

import javax.mail.MessagingException;
import java.io.IOException;

public interface EmailServices {

    void sendForgottenPasswordEmail(User recipient) throws MessagingException, IOException;
    boolean isDeliverable(String email) throws IOException;
}
