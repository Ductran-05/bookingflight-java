package com.bookingflight.app.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bookingflight.app.dto.response.APIResponse;

@RestControllerAdvice
public class GlobalException {
    // xu ly exception thong thuong
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<APIResponse<String>> handleAppException(AppException exception) {
        APIResponse<String> response = new APIResponse<>();
        response.setMessage(exception.getErrorCode().getMessage());
        response.setCode(exception.getErrorCode().getCode());
        return ResponseEntity.badRequest().body(response);
    }

}