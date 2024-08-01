package com.example.practice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CustomSecurityException extends RuntimeException{
    private final String errorCode;
    private final String errorMessage;
}
