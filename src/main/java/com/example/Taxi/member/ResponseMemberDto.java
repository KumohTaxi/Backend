package com.example.Taxi.member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseMemberDto {
    private String nickName;
    private String gender;

    @Builder
    public ResponseMemberDto(String nickName, String gender) {
        this.nickName = nickName;
        this.gender = gender;
    }
}
