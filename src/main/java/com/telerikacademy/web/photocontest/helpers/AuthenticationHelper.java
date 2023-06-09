package com.telerikacademy.web.photocontest.helpers;

import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.services.contracts.UserServices;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Component
@AllArgsConstructor
public class AuthenticationHelper {

    private static final String INVALID_AUTHENTICATION_ERROR = "Invalid authentication";
    private static final String INVALID_USERNAME_PASSWORD = "Invalid username/password";
    private final UserServices userServices;
    private final BCryptPasswordEncoder passwordEncoder;


    public User tryGetUser(Optional<String> authorization) {
        if (authorization.isPresent()) {
            try {
                String[] credentials = validateHeaderValues(authorization.get());
                User user = userServices.getByUsername(credentials[0]);
                if (!passwordEncoder.matches(credentials[1], user.getPassword())) {
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
    public User tryGetUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
        return user;
    }
    public User tryGetOrganizer(HttpSession session) {
        User user = tryGetUser(session);
        if (!user.isOrganizer()) {
            throw new UnauthorizedOperationException("Only organizers can access those resources.");
        }
        return user;
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
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new AuthorizationException(INVALID_USERNAME_PASSWORD);
            }
            return user;
        } catch (EntityNotFoundException e) {
            throw new AuthorizationException(INVALID_USERNAME_PASSWORD);
        }
    }
}
