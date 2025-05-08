package com.bookingflight.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @ExceptionHandler(value = {
            UsernameNotFoundException.class,
            BadCredentialsException.class })
    public ResponseEntity<APIResponse<Object>> handleIdException(Exception ex) {
        APIResponse<Object> res = new APIResponse<Object>();
        res.setMessage(ex.getMessage());
        res.setCode(HttpStatus.BAD_REQUEST.value());
        res.setData(null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<APIResponse<String>> handleAppException() {
        APIResponse<String> response = new APIResponse<>();
        response.setMessage(ErrorCode.UNIDENTIFIED_EXCEPTION.getMessage());
        response.setCode(ErrorCode.UNIDENTIFIED_EXCEPTION.getCode());
        return ResponseEntity.badRequest().body(response);
    }

}