package com.example.Taxi.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomExceptionStatus {

    NOT_FOUND_VALUE(1000, "원하는 값을 찾을 수 없습니다"),
    IMPOSSIBLE_ENTER_GROUP(1001,"이미 입장하거나 생성한 그룹이 존재합니다."),
    NOT_EXIST_GROUP(1002,"속한 그룹이 존재하지 않습니다."),
    INVALID_TOKEN(1003,"유효하지 않은 토큰입니다.");

    private final int errorCode;
    private final String message;

}
