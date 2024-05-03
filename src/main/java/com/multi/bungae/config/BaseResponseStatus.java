package com.multi.bungae.config;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BaseResponseStatus {
    /* ~1000 : success */
    SUCCESS(200, HttpStatus.OK, "요청에 성공하였습니다.");


    private final int code;
    private final HttpStatus httpStatus;
    private final String message;

    BaseResponseStatus(int code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}