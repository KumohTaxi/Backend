package com.example.Taxi.member;

import com.example.Taxi.config.exception.CustomException;
import com.example.Taxi.config.exception.CustomExceptionStatus;
import com.example.Taxi.token.JwtTokenProvider;
import com.example.Taxi.token.TokenDto;
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
            throw new CustomException(CustomExceptionStatus.INVALID_TOKEN);
        } else {
            TokenDto token = jwtTokenProvider.createToken(jwtTokenProvider.getIdentityNumByRefreshToken(tokenDto.getRefreshToken()));
            jwtTokenProvider.update(jwtTokenProvider.getIdentityNumByRefreshToken(tokenDto.getRefreshToken()), token.getAccessToken());
            return token;
        }
    }

    @PostMapping("/member/id")
    public Long getId(@RequestBody TokenDto tokenDto) {
        return memberService.findMemberByIdentityNum(
                jwtTokenProvider.getIdentityNumByAccessToken(tokenDto.getAccessToken())).getIdentityNum();
    }

    @PostMapping("/member/unlink")
    public void removeMember(@RequestBody TokenDto tokenDto) {
        memberService.remove(tokenDto);
    }
}
