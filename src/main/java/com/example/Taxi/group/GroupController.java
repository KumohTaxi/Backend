package com.example.Taxi.group;

import com.example.Taxi.config.exception.CustomException;
import com.example.Taxi.config.exception.CustomExceptionStatus;
import com.example.Taxi.post.PostResDto;
import com.example.Taxi.token.TokenDto;
import com.example.Taxi.member.Member;
import com.example.Taxi.post.Post;
import com.example.Taxi.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


/**
 * TODO accessToken의 유효성 검증이 필요하다!!
 */

@Slf4j
@RestController
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final MemberService memberService;

    @PostMapping("/group/new")
    public void createGroup(@RequestBody @Valid GroupRequestDto groupReqDto) {
        Member member = memberService.findMemberByAccessToken(groupReqDto.getAccessToken());
        if (member.getGroup() == null) groupService.createGroup(groupReqDto, member);
        else throw new CustomException(CustomExceptionStatus.IMPOSSIBLE_ENTER_GROUP);
    }

    @PostMapping("/group")
    public List<GroupResponseDto> findGroup(@RequestBody TokenDto tokenDto) {
        return groupService.findGroups(tokenDto);
    }

    @PostMapping("/group/filter/{destination}")
    public List<GroupResponseDto> findGroupByFilter(@PathVariable String destination, @RequestBody TokenDto tokenDto) {
        return groupService.findGroupsByDst(destination, tokenDto);
    }

    @GetMapping("/group/member/{accessToken}")
    public GroupResponseDto findMyGroup(@PathVariable String accessToken) {
        return groupService.findMyGroup(accessToken);
    }

    @PostMapping("/group/{id}")
    public Long enterGroup(@PathVariable Long id, @RequestBody TokenDto tokenDto) {
        log.info(tokenDto.getAccessToken());
        return groupService.enter(id, tokenDto.getAccessToken());
    }

    @GetMapping("/group/{id}")
    public GroupResponseDto findGroupById(@PathVariable Long id) {
        return new GroupResponseDto(groupService.findOne(id));
    }

    @PostMapping("/group/{id}/exit")
    public void exitGroup(@PathVariable Long id, @RequestBody TokenDto tokenDto) {
        groupService.exit(id, tokenDto.getAccessToken());
    }

    @GetMapping("/group/{id}/post")
    public List<PostResDto> getPost(@PathVariable Long id) {
        List<PostResDto> postResDtos = new ArrayList<>();
        for(Post post : groupService.findOne(id).getPosts()){
            postResDtos.add(new PostResDto(post));
        }
        return postResDtos;
    }
}
