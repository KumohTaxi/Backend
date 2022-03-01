package com.example.Taxi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello(){
        return "김성은 신영한 구현우 Taxi 프로젝트 시작 \n" + new Date();
    }
}
