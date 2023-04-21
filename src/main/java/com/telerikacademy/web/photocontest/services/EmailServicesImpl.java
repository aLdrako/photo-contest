package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.services.contracts.EmailServices;
import lombok.AllArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

@Service
@AllArgsConstructor
public class EmailServicesImpl implements EmailServices {
    private final JavaMailSender emailSender;
    @Override
    public void sendForgottenPasswordEmail(User recipient) throws MessagingException, IOException {
        //if (!isDeliverable(recipient.getEmail())) {
        //    throw new MessagingException("Email does not exist! Please change your email to an existing one!");
        //}
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(recipient.getEmail());
        helper.setSubject("Forgotten Password Reset");
        helper.setText(String.format("""
                Hello, '%s'
                
                It appears that you have forgotten your password.
                Here's a link to reset it:
               
                http://localhost:8080/auth/changepassword/%s
                
                Best Regards,
                Photo Contest Team!
                    
                """, recipient.getUsername(), recipient.getUsername()));

        emailSender.send(message);
    }

    @Override
    public boolean isDeliverable(String email) throws IOException {
        String apiKey = "2fd2cad14c484c3b901af704287103f1";
        String url = "https://api.zerobounce.net/v2/validate?api_key=" + apiKey + "&email=" + email;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        JSONObject jsonObject = new JSONObject(responseBody);
        return jsonObject.getString("status").equals("valid");
    }
}
