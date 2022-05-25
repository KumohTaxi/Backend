package com.example.Taxi.post;

import com.example.Taxi.config.exception.CustomException;
import com.example.Taxi.config.exception.CustomExceptionStatus;
import com.example.Taxi.member.Member;
import com.example.Taxi.token.JwtTokenProvider;
import com.example.Taxi.group.Group;
import com.example.Taxi.group.GroupRepo;
import com.example.Taxi.member.MemberRepo;
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
        Long identityNum = jwtTokenProvider.getIdentityNumByAccessToken(postReqDto.getAccessToken());
        Member member = memberRepo.findMemberByIdentityNum(identityNum)
                .orElseThrow(()->new CustomException(CustomExceptionStatus.INVALID_IDENTITY_NUM));

        Post post = Post.builder()
                .msg(postReqDto.getMsg())
                .postTime(LocalDateTime.now())
                .member(member)
                .group(group)
                .build();
        postRepo.save(post);
        group.post(post);
    }
}
