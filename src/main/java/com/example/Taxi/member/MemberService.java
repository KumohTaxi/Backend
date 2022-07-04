package com.example.Taxi.member;


import com.example.Taxi.config.exception.CustomException;
import com.example.Taxi.config.exception.CustomExceptionStatus;
import com.example.Taxi.token.JwtTokenProvider;
import com.example.Taxi.KaKaoApI;
import com.example.Taxi.token.Token;
import com.example.Taxi.token.TokenDto;
import com.example.Taxi.token.TokenRepo;
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
    private final TokenRepo tokenRepo;

    public TokenDto requestTokenToKakao(String authCode) {
        TokenDto token = kaKaoApI.getToken(authCode);
        return token;
    }

    @Transactional
    public TokenDto login(String accessTokenByKakao) {
        Member member = kaKaoApI.getUserInfo(accessTokenByKakao);
        if(member.getGender() == Gender.NONE){
            return new TokenDto("genderless", "genderless");
        }
        memberRepo.save(member);
        log.info("로그인 완료!!" + member.getIdentityNum() +" "+ member.getNickname());
        TokenDto tokenByService = jwtTokenProvider.createToken(member.getIdentityNum());
        jwtTokenProvider.save(new Token(member.getIdentityNum(),tokenByService.getAccessToken()));
        return tokenByService;
    }

    public Member findMemberByIdentityNum(Long identityNum) {
        return memberRepo.findMemberByIdentityNum(identityNum).orElseThrow(()->new CustomException(CustomExceptionStatus.INVALID_IDENTITY_NUM));
    }

    public Member findMemberByAccessToken(String accessToken) {
        return memberRepo.findMemberByIdentityNum(
                jwtTokenProvider.getIdentityNumByAccessToken(accessToken)).orElseThrow(()->new CustomException(CustomExceptionStatus.INVALID_IDENTITY_NUM));
    }

    @Transactional
    public void remove(TokenDto tokenDto) {
        Member member = memberRepo.findMemberByIdentityNum(
                jwtTokenProvider.getIdentityNumByAccessToken(tokenDto.getAccessToken())).orElseThrow(()->new CustomException(CustomExceptionStatus.INVALID_IDENTITY_NUM));
        kaKaoApI.unlink(member.getAccessTokenKaKao());
        Token token = tokenRepo.findTokenByAccessToken(tokenDto.getAccessToken())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.INVALID_TOKEN));
        tokenRepo.remove(token);
    }

}
