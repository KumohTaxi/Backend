package com.example.Taxi.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Member {
    @Id @GeneratedValue
    private Long id;
    private Long identityNum;
    private String nickname;
    private String accessTokenKaKao;
    private String refreshTokenKaKao;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY) @JoinColumn(name = "groups_id")
    private Group group;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public Member(Long identityNum, String nickname, String accessTokenKaKao, String refreshTokenKaKao, Gender gender, Status status) {
        this.identityNum = identityNum;
        this.nickname = nickname;
        this.accessTokenKaKao = accessTokenKaKao;
        this.refreshTokenKaKao = refreshTokenKaKao;
        this.gender = gender;
        this.status = status;
    }

    public void updateTokens(String accessTokenKaKao, String refreshTokenkaKao) {
        this.accessTokenKaKao = accessTokenKaKao;
        this.refreshTokenKaKao = refreshTokenkaKao;
    }

    public void joinGroup(Group group) {
        this.group = group;
        this.status = Status.CREW;
    }

    public void makeGroup(Group group){
        this.group = group;
        this.status = Status.CAPTAIN;
    }

    public void exitGroup() {
        this.group = null;
        this.status = Status.NONE;
    }

    public void promoteStatus() {
        this.status = Status.CAPTAIN;
    }

    public void updateUserInfo(Long identityNum, Gender gender) {
        this.identityNum = identityNum;
        this.gender = gender;
    }
}
