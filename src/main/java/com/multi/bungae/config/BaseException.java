package com.multi.bungae.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseException extends Exception {
    private BaseExceptionStatus exception;  //BaseExceptionStatus 객체에 매핑
}
