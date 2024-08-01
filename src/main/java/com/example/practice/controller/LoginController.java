package com.example.practice.controller;

import com.example.practice.dto.ResponseDto;
import com.example.practice.model.request.LoginRequest;
import com.example.practice.model.response.LoginResponse;
import com.example.practice.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    @Operation(summary = "Login API")
    public ResponseDto<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        log.info("Received request for login {}", loginRequest);
        return loginService.login(loginRequest);
    }
}
