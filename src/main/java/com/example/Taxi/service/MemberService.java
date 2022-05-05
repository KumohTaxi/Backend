package com.example.Taxi.service;

import com.example.Taxi.AdditionInfoDto;
import com.example.Taxi.JwtTokenProvider;
import com.example.Taxi.KaKaoApI;
import com.example.Taxi.controller.TokenDto;
import com.example.Taxi.domain.Member;
import com.example.Taxi.domain.Token;
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
    private final JwtTokenProvider jwtTokenProvider;

    public TokenDto requestTokenToKakao(String authCode) {
        TokenDto token = kaKaoApI.getToken(authCode);
        return token;
    }

    @Transactional
    public TokenDto login(String accessTokenByKakao) {
        Member member = kaKaoApI.getUserInfo(accessTokenByKakao);
        memberRepo.save(member);
        log.info("로그인 완료!!" + member.getId() +" "+ member.getNickname());
        TokenDto tokenByService = jwtTokenProvider.createToken(member.getIdentityNum());
        jwtTokenProvider.save(new Token(member.getIdentityNum(),tokenByService.getAccessToken()));
        return tokenByService;
    }

    public Member findMemberByIdentityNum(Long identityNum) {
        //예외처리 필요 + Optional로 처리
        return memberRepo.findMemberByIdentityNum(identityNum).get(0);
    }

    public Member findMemberByAccessToken(String accessToken) {
        //예외처리 필요 + Optional로 처리
        return memberRepo.findMemberByIdentityNum(
                jwtTokenProvider.getIdentityNumByAccessToken(accessToken)).get(0);
    }

    @Transactional
    public void getAdditionInfo(String accessToken) {
        Member member = memberRepo.findMemberByIdentityNum(
                jwtTokenProvider.getIdentityNumByAccessToken(accessToken)).get(0);
        AdditionInfoDto userInfo = kaKaoApI.getUserGender(member.getAccessTokenKaKao());

        log.info("Before: " + member.getIdentityNum() + "After: " + userInfo.getIdentityNum());
        log.info("Before: " + member.getGender() + "After: " + userInfo.getGender());

        member.updateUserInfo(userInfo.getIdentityNum(),userInfo.getGender());
    }
}
