package com.example.practice.util;

import com.example.practice.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
@Component
public class DateUtil {

    public static Date conversion(Date lastLogin) {
        try {
            SimpleDateFormat outputFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
            String formattedDate = outputFormat.format(lastLogin);
            return outputFormat.parse(formattedDate);
        } catch (Exception ex) {
            log.error("There is some error in conversion of date: {}", ex.getMessage());
            throw new CustomException(ErrorConstants.SYSTEM_ERROR_CODE, ex.getMessage());
        }
    }

    public static Date getDate() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        return new Date();
    }
}
