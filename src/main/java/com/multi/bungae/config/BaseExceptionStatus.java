package com.multi.bungae.config;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
public enum BaseExceptionStatus {
    SUCCESS(200, HttpStatus.OK, "요청에 성공하였습니다."),
    BAD_REQUEST(400,  HttpStatus.BAD_REQUEST, "입력값을 확인해주세요."),
    FORBIDDEN(403, HttpStatus.FORBIDDEN, "권한이 없습니다."),
    NOT_FOUND(404, HttpStatus.NOT_FOUND, "대상을 찾을 수 없습니다."),
    DATABASE_ERROR(500, INTERNAL_SERVER_ERROR, "데이터베이스 오류입니다."),

    EMPTY_ID(1001, HttpStatus.BAD_REQUEST, "아이디 값이 비어있습니다."),
    INVALID_ID(1002, HttpStatus.BAD_REQUEST , "아이디 형식이 올바르지 않습니다."),
    ID_ALREADY_EXISTS(409, HttpStatus.CONFLICT, "중복된 아이디 입니다."),

    EMPTY_PASSWORD(1003, HttpStatus.BAD_REQUEST, "비밀번호 값이 비어있습니다."),
    INVALID_PASSWORD(1004, HttpStatus.BAD_REQUEST , "비밀번호 형식이 올바르지 않습니다."),
    PASSWORD_ENCRYPTION_FAILURE(500, INTERNAL_SERVER_ERROR, "비밀번호 암호화에 실패했습니다."),

    EMPTY_EMAIL(1005, HttpStatus.BAD_REQUEST, "이메일 값이 비어있습니다."),
    INVALID_EMAIL(1006, HttpStatus.BAD_REQUEST , "이메일 형식이 올바르지 않습니다."),
    NOT_FOUND_EMAIL(1007, HttpStatus.BAD_REQUEST , "가입된 이메일을 찾을 수 없습니다."),

    EMPTY_NICKNAME(1008, HttpStatus.BAD_REQUEST, "닉네임 값이 비어있습니다."),
    INVALID_NICKNAME(1009, HttpStatus.BAD_REQUEST , "닉네임 형식이 올바르지 않습니다."),

    EMPTY_NAME(1010, HttpStatus.BAD_REQUEST, "이름 값이 비어있습니다."),
    INVALID_NAME(1011, HttpStatus.BAD_REQUEST , "이름 형식이 올바르지 않습니다."),

    EMPTY_GENDER(1012, HttpStatus.BAD_REQUEST, "성별 값이 비어있습니다."),
    INVALID_GENDER(1013, HttpStatus.BAD_REQUEST , "성별 형식이 올바르지 않습니다."),

    EMPTY_BIRTH(1014, HttpStatus.BAD_REQUEST, "생년월일 형식이 올바르지 않습니다."),
    INVALID_BIRTH(1015, HttpStatus.BAD_REQUEST, "생년월일 정규 표현식 예외입니다."),

    INVALID_TEL(1016, HttpStatus.BAD_REQUEST, "핸드폰번호 정규 표현식 예외입니다."),
    EMPTY_TEL(1017,HttpStatus.BAD_REQUEST, "전화번호 값이 비어있습니다."),

    LOGIN_FAILED(1018, HttpStatus.BAD_REQUEST, "로그인 실패");

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;

    BaseExceptionStatus(int code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}