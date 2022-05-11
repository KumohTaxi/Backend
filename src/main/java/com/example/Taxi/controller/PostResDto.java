package com.example.Taxi.controller;

import com.example.Taxi.domain.Post;
import com.example.Taxi.domain.Status;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResDto {
    private Long identityNum;
    private Status memberStatus;
    private String msg;
    private LocalDateTime postTime;

    public PostResDto(Post post) {
        this.identityNum = post.getMember().getIdentityNum();
        this.memberStatus = post.getMember().getStatus();
        this.msg = post.getMsg();
        this.postTime = post.getPostTime();
    }
}
