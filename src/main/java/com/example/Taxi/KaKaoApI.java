package com.example.Taxi;

import com.example.Taxi.controller.TokenDto;
import com.example.Taxi.domain.Gender;
import com.example.Taxi.domain.Member;
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
import java.util.HashMap;

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
            sb.append("&code=" + authCode.substring(13,authCode.length()-2));
            bw.write(sb.toString());
            bw.flush();
            log.info(sb.toString());
            int responseStatus = conn.getResponseCode();
            log.info("ResponseStatus: " + responseStatus);
            log.info("ResponseBody " + conn.getResponseMessage());

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = ""; String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info("response body: " + result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

            log.info("access_token : " + accessToken);
            log.info("refresh_token : " + refreshToken);

            br.close();
            bw.close();

        } catch (IOException e){
            e.printStackTrace();
        }

        return new TokenDto(accessToken,refreshToken);
    }

    public Member getUserInfo(String accessToken) {

        // 요청하는 클라이언트마다 가진 정보가 다를 수 있기에 HashMap타입으로 선언
        HashMap<String, Object> userInfo = new HashMap<String, Object>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // 요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
            log.info("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info("response body : " + result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            nickname = properties.getAsJsonObject().get("nickname").getAsString();
            gender = Gender.valueOf(kakao_account.getAsJsonObject().get("gender").getAsString().toUpperCase());


        } catch (IOException e) {
            e.printStackTrace();
        }

        return Member.builder()
                .nickname(nickname)
                .accessTokenKaKao(this.accessToken)
                .refreshTokenKaKao(refreshToken)
                .gender(gender)
                .build();
    }

    public void logout(){
        String reqeURL = "https://kapi.kakao.com/v1/user/logout";
        try {
            URL url = new URL(reqeURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String result = "";
            String line = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
