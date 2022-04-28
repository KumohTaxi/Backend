package com.example.Taxi;

import com.example.Taxi.controller.TokenDto;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtTokenProvider {
    @Value("${key.token}")
    private String key;

    public TokenDto createToken(int identityNum) {
        String accessToken = makeJwt(-1);
        String refreshToken = makeJwt(identityNum);
        return new TokenDto(accessToken, refreshToken);
    }

    public String makeJwt(int identityNum){
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
            return false;
        }
    }

    public int getIdentityNum(String refreshToken) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(refreshToken).getBody()
                .get("identityNum", Integer.class);
    }
}
