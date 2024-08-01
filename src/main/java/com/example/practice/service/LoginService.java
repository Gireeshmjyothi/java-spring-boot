package com.example.practice.service;

import com.example.practice.auth.security.AuthService;
import com.example.practice.auth.security.JwtService;
import com.example.practice.dao.UserDao;
import com.example.practice.dto.ResponseDto;
import com.example.practice.entity.User;
import com.example.practice.exception.CustomException;
import com.example.practice.model.request.LoginRequest;
import com.example.practice.model.response.LoginResponse;
import com.example.practice.util.AppConstants;
import com.example.practice.util.ErrorConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {
    private final UserDao userDao;
    private final AuthService authService;

    public ResponseDto<LoginResponse> login(LoginRequest loginRequest){
        User user =  userDao.getByUserName(loginRequest.getUserName())
                .orElseThrow(()-> new CustomException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "User")));
        log.info("Validate password of user data");
        if (!(loginRequest.getPassword().equalsIgnoreCase(user.getPassword()))) {
            userDao.updateLoginAttempt(user, false);
            throw new CustomException(ErrorConstants.INVALID_ERROR_CODE, MessageFormat.format(ErrorConstants.INVALID_ERROR_CODE_MESSAGE, "Password"));
        }
        userDao.updateLoginAttempt(user, true);
        String token = authService.generateJwtToken(user);

        return ResponseDto.<LoginResponse>builder()
                .status(AppConstants.RESPONSE_SUCCESS)
                .data(List.of(LoginResponse.builder()
                                .token(token)
                        .build()))
                .build();
    }
}
