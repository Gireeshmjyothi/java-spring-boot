package com.example.practice.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class AppConfig {

    @Value("${app.sec.key}")
    private String appSecurityKey;

    @Value("${token.expiry.time.hr}")
    private int tokenExpiryTime;

}
