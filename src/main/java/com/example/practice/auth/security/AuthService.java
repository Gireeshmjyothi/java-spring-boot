package com.example.practice.auth.security;

import com.example.practice.entity.User;
import com.example.practice.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private int tokenExpiryTimeHr;

    @Value("${token.expiry.time.hr}")
    public void setTokenExpiryTimeHr(int tokenExpiryTimeHr) {
        this.tokenExpiryTimeHr = tokenExpiryTimeHr;
    }

    public String generateJwtToken(User authenticatedUser) {
        return generateToken(authenticatedUser, tokenExpiryTimeHr);
    }

    private String generateToken(User authenticatedUser, int tokenExpiryTimeHr) {
        Date expiryTime = DateUtils.addHours(DateUtil.getDate(), tokenExpiryTimeHr);
        String jwtToken = createJWTToken(authenticatedUser, expiryTime);
        log.debug("jwtToken {}", jwtToken);
        return jwtToken;
    }


    private String createJWTToken(User authenticatedUser, Date expiryTime) {
        return jwtService.generateToken(authenticatedUser, expiryTime);
    }

}
