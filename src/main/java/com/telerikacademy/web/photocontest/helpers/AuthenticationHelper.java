package com.telerikacademy.web.photocontest.helpers;

import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.services.contracts.UserServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class AuthenticationHelper {

    private static final String INVALID_AUTHENTICATION_ERROR = "Invalid authentication";
    private static final String INVALID_USERNAME_PASSWORD = "Invalid username/password";
    private final UserServices userServices;


    public User tryGetUser(Optional<String> authorization) {
        if (authorization.isPresent()) {
            try {
                String[] credentials = validateHeaderValues(authorization.get());
                User user = userServices.getByUsername(credentials[0]);
                if (!user.getPassword().equals(credentials[1])) {
                    throw new AuthorizationException(INVALID_USERNAME_PASSWORD);
                }
                return user;
            } catch (EntityNotFoundException e) {
                throw new AuthorizationException(e.getMessage());
            }
        } else {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
    }

    private String[] validateHeaderValues(String headerValue) {
        String[] credentials = headerValue.split("[, ]+", -1);
        if (!headerValue.contains(",") && !headerValue.contains(" ")
                || credentials[0].strip().length() == 0 || credentials[1].strip().length() == 0) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
        credentials[0] = credentials[0].strip();
        credentials[1] = credentials[1].strip();
        return credentials;

    }

    public User verifyLogin(String username, String password) {
        try {
            User user = userServices.getByUsername(username);
            if (!user.getPassword().equals(password)) {
                throw new AuthorizationException(INVALID_USERNAME_PASSWORD);
            }
            return user;
        } catch (EntityNotFoundException e) {
            throw new AuthorizationException(INVALID_USERNAME_PASSWORD);
        }
    }
}
