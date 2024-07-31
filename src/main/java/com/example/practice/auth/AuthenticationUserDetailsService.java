package com.example.practice.auth;

import com.example.practice.exception.CustomException;
import com.example.practice.service.UserService;
import com.example.practice.util.ErrorConstants;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;


@Service
public class AuthenticationUserDetailsService {
    private final UserService userService;

    public AuthenticationUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    public UserDetailsService userDetailsService() {
        return username -> userService.getUserByUserName(username)
                .orElseThrow(() -> new CustomException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "User")));
    }
}
