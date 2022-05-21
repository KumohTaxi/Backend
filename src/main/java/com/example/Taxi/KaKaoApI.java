package com.example.Taxi;

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

            log.info(sb.toString());
            log.info("ResponseCode: " + conn.getResponseCode());

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = ""; String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }

            JsonElement element = JsonParser.parseString(result);

            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

            br.close();
            bw.close();

        } catch (IOException e){
            e.printStackTrace();
        }

        return new TokenDto(accessToken,refreshToken);
    }

    public Member getUserInfo(String accessToken) {

        try {
            URL url = new URL("https://kapi.kakao.com/v2/user/me");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            log.info("responseCode : " + conn.getResponseCode());

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
            e.printStackTrace();
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
            log.info("responseCode : " + conn.getResponseCode());

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String result = ""; String line = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info(result);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unlink(String accessToken){

        try {
            URL url = new URL("https://kapi.kakao.com/v1/user/unlink");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            log.info("연결 끊기 responseCode : " + conn.getResponseCode());

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String result = ""; String line = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info(result);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
