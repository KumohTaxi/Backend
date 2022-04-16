package com.example.Taxi.controller;

import com.example.Taxi.domain.Member;
import com.example.Taxi.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@Slf4j
@AllArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    @ResponseBody
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public String start(@RequestBody Member member){
        log.info(member.getId().getClass().getName());
        log.info(member.getId() + member.getPassword());
        if(member.getPassword().equals("1234"))
            return "success";
        return "fail";
    }

    @PostMapping("/auth/kakao")
    public ResponseMemberDto login(@RequestParam(value = "authCode") String authCode){
        log.info("authCode: " + authCode);
        HashMap<String, Object> userInfo = memberService.getUserInfo(memberService.getToken(authCode));
        ResponseMemberDto responseMemberDto = new ResponseMemberDto(userInfo.get("nickname").toString(), userInfo.get("gender").toString());
        log.info("###nickname#### : " + responseMemberDto.getNickName());
        log.info("###email#### : " + responseMemberDto.getGender());


        return responseMemberDto;
    }


}
