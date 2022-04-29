package com.example.Taxi.controller;

import com.example.Taxi.JwtTokenProvider;
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
    public TokenDto login(@RequestBody String authCode){
        log.info("authCode: " + authCode);

        TokenDto tokenByKaKaoDto = memberService.requestTokenToKakao(authCode);
        return memberService.login(tokenByKaKaoDto.getAccessToken());
    }

    @PostMapping("/member/token")
    public TokenDto reissue(@RequestBody String refreshToken) throws Exception {
        if(!jwtTokenProvider.validateToken(refreshToken)){
            throw new Exception("유효하지 않은 토큰입니다.");
        }
        else{
            TokenDto token = jwtTokenProvider.createToken(jwtTokenProvider.getIdentityNumByRefreshToken(refreshToken));
            jwtTokenProvider.update(jwtTokenProvider.getIdentityNumByRefreshToken(refreshToken),token.getAccessToken());
            return token;
        }
    }


}
