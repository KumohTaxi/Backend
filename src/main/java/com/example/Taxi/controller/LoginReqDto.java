package com.example.Taxi.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginReqDto {
    private String authCode;

    public LoginReqDto(String authCode) {
        this.authCode = authCode;
    }
}
