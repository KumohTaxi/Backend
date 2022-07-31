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

    @PostMapping("/auth/kakao")
    public TokenDto login(@RequestBody LoginReqDto loginReqDto) {

        TokenDto tokenByKaKaoDto = memberService.requestTokenToKakao(loginReqDto.getAuthCode());
        return memberService.login(tokenByKaKaoDto.getAccessToken());
    }

    @PostMapping("/member/token")
    public TokenDto reissue(@RequestBody TokenDto tokenDto) throws Exception {
        return memberService.reissue(tokenDto);
    }

    @PostMapping("/member/id")
    public Long getId(@RequestBody TokenDto tokenDto) {
        return memberService.findMemberByIdentityNum(tokenDto).getIdentityNum();
    }

    @PostMapping("/member/unlink")
    public void removeMember(@RequestBody TokenDto tokenDto) {
        memberService.remove(tokenDto);
    }
}
