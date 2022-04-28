package com.example.Taxi.service;

import com.example.Taxi.KaKaoApI;
import com.example.Taxi.controller.TokenDto;
import com.example.Taxi.domain.Member;
import com.example.Taxi.repo.MemberRepo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Getter
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberService {

    private final KaKaoApI kaKaoApI;
    private final MemberRepo memberRepo;

    public TokenDto requestTokenToKakao(String authCode) {
        TokenDto token = kaKaoApI.getToken(authCode);
        return token;
    }

    @Transactional
    public int login(String accessTokenByKakao) {
        Member member = kaKaoApI.getUserInfo(accessTokenByKakao);
        memberRepo.save(member);
        return member.getIdentityNum();
    }

    public Member findMember(String accessToken) {
        return memberRepo.findByAccessToken(accessToken).get(0);
    }
}
