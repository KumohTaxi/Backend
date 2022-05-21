package com.example.Taxi.post;

import lombok.RequiredArgsConstructor;
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
