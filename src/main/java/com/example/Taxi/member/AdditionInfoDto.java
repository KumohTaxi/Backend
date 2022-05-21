package com.example.Taxi.member;

import com.example.Taxi.member.Gender;
import lombok.Getter;

@Getter
public class AdditionInfoDto {
    private Long identityNum;
    private Gender gender;

    public AdditionInfoDto(Long identityNum, Gender gender) {
        this.identityNum = identityNum;
        this.gender = gender;
    }
}
