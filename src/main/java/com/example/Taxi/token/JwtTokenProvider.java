package com.example.Taxi.token;

import com.example.Taxi.config.exception.CustomException;
import com.example.Taxi.config.exception.CustomExceptionStatus;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Transactional
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${key.token}")
    private String key;

    private final TokenRepo tokenRepo;

    public TokenDto createToken(Long identityNum) {
        String accessToken = makeJwt(0L);
        String refreshToken = makeJwt(identityNum);
        return new TokenDto(accessToken, refreshToken);
    }

    public String makeJwt(Long identityNum){
        Date now = new Date();
        if(identityNum > -1){
            //refreshToken 생성 및 반환
            return Jwts.builder()
                    .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                    .setIssuedAt(now)
                    .setExpiration(new Date(now.getTime() + Duration.ofMinutes(Integer.valueOf(30)).toMillis()))
                    .claim("identityNum",identityNum)
                    .signWith(SignatureAlgorithm.HS256,key)
                    .compact();
        }
        else{
            //accessToken 생성 및 반환
            return Jwts.builder()
                    .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                    .setIssuedAt(now)
                    .setExpiration(new Date(now.getTime() + Duration.ofMinutes(Integer.valueOf(5)).toMillis()))
                    .signWith(SignatureAlgorithm.HS256,key)
                    .compact();
        }
    }

    public boolean validateToken(String jwtToken) {
        try{
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwtToken);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (JwtException e){
            log.info(e.toString());
            throw new CustomException(CustomExceptionStatus.INVALID_TOKEN);
        }
    }

    public Long getIdentityNumByRefreshToken(String refreshToken) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(refreshToken).getBody()
                .get("identityNum", Long.class);
    }

    public Long getIdentityNumByAccessToken(String accessToken) {
        return tokenRepo.findIdentityNumByAccessToken(accessToken)
                .orElseThrow(()->new CustomException(CustomExceptionStatus.INVALID_TOKEN));
    }

    public void update(Long identityNum, String AccessToken) {
        Token token = tokenRepo.findTokenByIdentityNum(identityNum)
                .orElseThrow(()->new CustomException(CustomExceptionStatus.INVALID_IDENTITY_NUM));
        token.updateAccessToken(AccessToken);
    }

    public void save(Token token){
        Optional<Token> findToken = tokenRepo.findTokenByIdentityNum(token.getIdentityNum());
        if(findToken.isEmpty()) tokenRepo.save(token);
        else findToken.get().updateAccessToken(token.getAccessToken());
    }
}
