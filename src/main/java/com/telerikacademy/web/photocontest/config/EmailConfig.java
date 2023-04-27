package com.telerikacademy.web.photocontest.config;

import com.telerikacademy.web.photocontest.models.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.*;

@Configuration
public class EmailConfig {
    private final String host, port, username, password;
    private final boolean smtpAuth, starttlsEnable, mailDebug;

    public EmailConfig(Environment environment) {
        host = environment.getProperty("spring.mail.host");
        port = environment.getProperty("spring.mail.port");
        username = environment.getProperty("spring.mail.username");
        password = environment.getProperty("spring.mail.password");
        smtpAuth = Boolean.parseBoolean(environment.getProperty("spring.mail.properties.mail.smtp.auth"));
        starttlsEnable = Boolean.parseBoolean(environment.getProperty("spring.mail.properties.mail.smtp.starttls.enable"));
        mailDebug = Boolean.parseBoolean(environment.getProperty("mail.debug"));
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(Integer.parseInt(port));
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", smtpAuth);
        props.put("mail.smtp.starttls.enable", starttlsEnable);
        props.put("mail.debug", mailDebug);

        return mailSender;
    }
}
