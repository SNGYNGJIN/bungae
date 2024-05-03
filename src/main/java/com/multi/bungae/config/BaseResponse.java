package com.multi.bungae.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class BaseResponse<T> {
    private final int code;
    private final HttpStatus httpStatus;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    // 요청에 성공한 경우
    public BaseResponse(T result) {
        this.code = BaseResponseStatus.SUCCESS.getCode();
        this.httpStatus = BaseResponseStatus.SUCCESS.getHttpStatus();
        this.message = BaseResponseStatus.SUCCESS.getMessage();
        this.result = result;
    }

    // 요청에 실패한 경우
    public BaseResponse(BaseExceptionStatus status) {
        this.code = status.getCode();
        this.httpStatus = status.getHttpStatus();
        this.message = status.getMessage();
    }
}
