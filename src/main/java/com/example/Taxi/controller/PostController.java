package com.example.Taxi.controller;

import com.example.Taxi.JwtTokenProvider;
import com.example.Taxi.service.GroupService;
import com.example.Taxi.service.MemberService;
import com.example.Taxi.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts/new")
    public void post(@RequestBody @Valid PostReqDto postReqDto) {
        postService.postMsg(postReqDto);
    }

}
