package com.example.Taxi.controller;

import com.example.Taxi.JwtTokenProvider;
import com.example.Taxi.domain.Token;
import com.example.Taxi.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@AllArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;


    @PostMapping("/auth/kakao")
    public TokenDto login(@RequestBody LoginReqDto loginReqDto) {
        log.info("authCode: " + loginReqDto.getAuthCode());

        TokenDto tokenByKaKaoDto = memberService.requestTokenToKakao(loginReqDto.getAuthCode());
        return memberService.login(tokenByKaKaoDto.getAccessToken());
    }

    @PostMapping("/member/token")
    public TokenDto reissue(@RequestBody TokenDto tokenDto) throws Exception {
        if (!jwtTokenProvider.validateToken(tokenDto.getRefreshToken())) {
            throw new Exception("유효하지 않은 토큰입니다.");
        } else {
            TokenDto token = jwtTokenProvider.createToken(jwtTokenProvider.getIdentityNumByRefreshToken(tokenDto.getRefreshToken()));
            jwtTokenProvider.update(jwtTokenProvider.getIdentityNumByRefreshToken(tokenDto.getRefreshToken()), token.getAccessToken());
            return token;
        }
    }

    //추가정보(gender 요청) 요청 메서드 생성
    @PostMapping("/member/gender")
    public void getGender(@RequestBody TokenDto tokenDto) {
        memberService.getAdditionInfo(tokenDto.getAccessToken());
    }

    @GetMapping("/member/id")
    public Long getId(@RequestBody TokenDto tokenDto) {
        return memberService.findMemberByIdentityNum(
                jwtTokenProvider.getIdentityNumByAccessToken(tokenDto.getAccessToken())).getIdentityNum();
    }
}
