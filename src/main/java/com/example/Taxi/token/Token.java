package com.example.Taxi.token;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor
public class Token {
    @Id @GeneratedValue
    private Long id;
    private Long identityNum;
    private String accessToken;

    @Builder
    public Token(Long identityNum, String accessToken) {
        this.identityNum = identityNum;
        this.accessToken = accessToken;
    }

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
