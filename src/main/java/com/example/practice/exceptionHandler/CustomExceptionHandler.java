package com.example.practice.exceptionHandler;

import com.example.practice.dto.ErrorDto;
import com.example.practice.dto.ResponseDto;
import com.example.practice.exception.CustomException;
import com.example.practice.exception.CustomSecurityException;
import com.example.practice.util.AppConstants;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomSecurityException.class)
    public ResponseEntity<Object> handleSecurityException(CustomSecurityException exp){
        ErrorDto errorDto = ErrorDto.builder()
                .errorCode(exp.getErrorCode())
                .errorMessage(exp.getErrorMessage())
                .build();
        return generateResponseWithErrors(List.of(errorDto));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .errorCode(ex.getErrorCode())
                .errorMessage(ex.getErrorMessage())
                .build();
        return generateResponseWithErrors(List.of(errorDto));
    }

    @ExceptionHandler(value = {JwtException.class})
    public ResponseEntity<Object> handleJwtException(JwtException ex, WebRequest request) {
        String requestUri = ((ServletWebRequest)request).getRequest().getRequestURI();
        logger.error("Error in Authorization ", ex);
        ErrorDto errorDto = ErrorDto.builder()
                .errorCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
                .errorMessage(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ResponseDto.builder()
                        .status(AppConstants.RESPONSE_FAILURE)
                        .errors(List.of(errorDto))
                        .build());
    }

    private ResponseEntity<Object> generateResponseWithErrors(List<ErrorDto> errors) {
        return ResponseEntity.ok().body(
                ResponseDto.builder()
                        .status(AppConstants.RESPONSE_FAILURE)
                        .errors(errors)
                        .build());
    }

}
