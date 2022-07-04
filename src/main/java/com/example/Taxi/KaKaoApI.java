package com.example.Taxi;

import com.example.Taxi.config.exception.CustomException;
import com.example.Taxi.config.exception.CustomExceptionStatus;
import com.example.Taxi.token.TokenDto;
import com.example.Taxi.member.Gender;
import com.example.Taxi.member.Member;
import com.example.Taxi.member.Status;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


@Slf4j
@Getter
@Component
public class KaKaoApI {

    @Value("${redirect_url}")
    private String redURL;
    @Value("${client_id}")
    private String client_id;
    private String accessToken;
    private String refreshToken;
    private Gender gender;
    private String nickname;
    private Long identityNum;

    public TokenDto getToken(String authCode) {

        try {
            URL url = new URL("https://kauth.kakao.com/oauth/token");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + client_id);
            sb.append("&redirect_url=" + redURL);
            sb.append("&code=" + authCode);
            bw.write(sb.toString());
            bw.flush();

            String line = ""; String result = "";
            if(conn.getResponseCode() != 200){
                log.error("kakao api getToken : " + conn.getResponseMessage());
                log.info(sb.toString());
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                while ((line = br.readLine()) != null) {
                    result += line;
                }
                br.close();
                JsonElement element = JsonParser.parseString(result);
                log.error("kakao api messageBody :" + element.toString());
                throw new CustomException(CustomExceptionStatus.INVALID_AUTH_CODE);
            } else{
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    result += line;
                }
                br.close();
                JsonElement element = JsonParser.parseString(result);
                accessToken = element.getAsJsonObject().get("access_token").getAsString();
                refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();
            }
            bw.close();

        } catch (IOException e){
            log.error(e.getMessage());
        }

        return new TokenDto(accessToken,refreshToken);
    }

    public Member getUserInfo(String accessToken) {

        try {
            URL url = new URL("https://kapi.kakao.com/v2/user/me");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            log.info("getUserInfo responseCode : " + conn.getResponseCode());

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = ""; String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }

            JsonElement element = JsonParser.parseString(result);
            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            identityNum = element.getAsJsonObject().get("id").getAsLong();
            nickname = properties.getAsJsonObject().get("nickname").getAsString();
            if(kakao_account.has("gender")){
                gender = Gender.valueOf(kakao_account.get("gender").getAsString().toUpperCase());
            } else{
                gender = Gender.NONE;
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return Member.builder()
                .identityNum(identityNum)
                .nickname(nickname)
                .accessTokenKaKao(this.accessToken)
                .refreshTokenKaKao(refreshToken)
                .gender(gender)
                .status(Status.NONE)
                .build();
    }

    public void logout(){

        try {
            URL url = new URL("https://kapi.kakao.com/v1/user/logout");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            log.info("logout responseCode : " + conn.getResponseCode());

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String result = ""; String line = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info(result);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void unlink(String accessToken){

        try {
            URL url = new URL("https://kapi.kakao.com/v1/user/unlink");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            log.info("kakao api: unlink responseCode : " + conn.getResponseCode());
            log.info("kakao api: unlink responseMessage: " + conn.getResponseMessage());

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String result = ""; String line = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info(result);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
