package com.example.practice.auth.security;

import com.example.practice.entity.User;
import com.example.practice.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

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
        Authentication authentication = authentication(authenticatedUser);
        Date expiryTime = DateUtils.addHours(DateUtil.getDate(), tokenExpiryTimeHr);
        String jwtToken = createJWTToken(authentication, authenticatedUser, expiryTime);
        log.debug("jwtToken {}", jwtToken);
        return jwtToken;
    }
    private Authentication authentication(User authenticatedUser) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticatedUser.getUsername(), authenticatedUser.getPassword()));
        log.debug("authenticate {}", authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    private String createJWTToken(Authentication authentication, User authenticatedUser, Date expiryTime) {
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        return jwtService.generateToken(authenticatedUser, expiryTime);
    }

}
