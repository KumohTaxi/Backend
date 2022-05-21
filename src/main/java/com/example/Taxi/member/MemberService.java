package com.example.Taxi.member;


import com.example.Taxi.token.JwtTokenProvider;
import com.example.Taxi.KaKaoApI;
import com.example.Taxi.post.TokenRepo;
import com.example.Taxi.token.Token;
import com.example.Taxi.token.TokenDto;
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
        //예외처리 필요 + Optional로 처리
        return memberRepo.findMemberByIdentityNum(identityNum).get(0);
    }

    public Member findMemberByAccessToken(String accessToken) {
        //예외처리 필요 + Optional로 처리
        return memberRepo.findMemberByIdentityNum(
                jwtTokenProvider.getIdentityNumByAccessToken(accessToken)).get(0);
    }

    @Transactional
    public void remove(TokenDto tokenDto) {
        Member member = memberRepo.findMemberByIdentityNum(
                jwtTokenProvider.getIdentityNumByAccessToken(tokenDto.getAccessToken())).get(0);
        kaKaoApI.unlink(member.getAccessTokenKaKao());
        tokenRepo.remove(tokenRepo.findTokenByAccessToken(tokenDto.getAccessToken()).get(0));
    }

}
