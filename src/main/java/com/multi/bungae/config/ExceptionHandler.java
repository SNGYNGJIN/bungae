package com.multi.bungae.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse<BaseException>> errorHandler(BaseException e) {
        log.error(e.getException().getMessage());
        return new ResponseEntity<>(new BaseResponse<>(e.getException()), e.getException().getHttpStatus());
    }
}
