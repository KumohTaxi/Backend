package com.example.Taxi.controller;

import com.example.Taxi.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Controller
public class LoginController {
    @ResponseBody
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public String start(@RequestBody Member member){
        log.info(member.getId().getClass().getName());
        log.info(member.getId() + member.getPassword());
        if(member.getPassword().equals("1234"))
            return "success";
        return "fail";
    }
}
