package com.example.Taxi.service;

import com.example.Taxi.JwtTokenProvider;
import com.example.Taxi.controller.PostReqDto;
import com.example.Taxi.domain.Group;
import com.example.Taxi.domain.Member;
import com.example.Taxi.domain.Post;
import com.example.Taxi.repo.GroupRepo;
import com.example.Taxi.repo.MemberRepo;
import com.example.Taxi.repo.PostRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepo postRepo;
    private final MemberRepo memberRepo;
    private final GroupRepo groupRepo;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void postMsg(PostReqDto postReqDto) {

        Group group = groupRepo.findById(postReqDto.getGroupId());
        Post post = Post.builder()
                .msg(postReqDto.getMsg())
                .postTime(LocalDateTime.now())
                .member(memberRepo.findMemberByIdentityNum(
                        jwtTokenProvider.getIdentityNumByAccessToken(postReqDto.getAccessToken())).get(0))
                .group(group)
                .build();
        postRepo.save(post);
        group.post(post);
    }
}
