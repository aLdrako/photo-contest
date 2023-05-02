package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.services.contracts.EmailServices;
import lombok.AllArgsConstructor;
import lombok.Getter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static com.telerikacademy.web.photocontest.helpers.RandomStringGenerator.generateString;

@Service
@Getter
@AllArgsConstructor
public class EmailServicesImpl implements EmailServices {
    private final JavaMailSender emailSender;
    private final Map<String, User> urlKeys;
    private final Map<User, String> usersWithKeys;


    @Override
    public void sendForgottenPasswordEmail(User recipient) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(recipient.getEmail());
        helper.setSubject("Forgotten Password Reset");
        String uniqueUrlKey = generateString();
        helper.setText(String.format("""
                Hello, '%s'
                
                It appears that you have forgotten your password.
                Here's a link to reset it:
               
                http://localhost:8080/auth/changepassword/%s
                
                Best Regards,
                Junkies Yard Team!
                    
                """, recipient.getUsername(), uniqueUrlKey));
        populateUrlKeys(recipient, uniqueUrlKey);
        emailSender.send(message);
    }

    @Override
    public void sendConfirmationEmail(User recipient) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(recipient.getEmail());
        helper.setSubject("Account/Email Verification");
        String uniqueUrlKey = generateString();
        helper.setText(String.format("""
                Hello, '%s'
                
                In order to verify your account/email you need to open the link below!
               
                http://localhost:8080/auth/confirmation/%s
                
                Best Regards,
                Junkies Yard Team!
                    
                """, recipient.getUsername(), uniqueUrlKey));
        populateUrlKeys(recipient, uniqueUrlKey);
        emailSender.send(message);
    }

    private void populateUrlKeys(User recipient, String uniqueUrlKey) {
        if  (usersWithKeys.containsKey(recipient)) {
            urlKeys.remove(usersWithKeys.get(recipient));
        }
        urlKeys.put(uniqueUrlKey, recipient);
        usersWithKeys.put(recipient, uniqueUrlKey);
    }

    @Override
    public void clearKey(String urlKey) {
        User user = urlKeys.get(urlKey);
        usersWithKeys.remove(user);
        urlKeys.remove(urlKey);
    }
}
