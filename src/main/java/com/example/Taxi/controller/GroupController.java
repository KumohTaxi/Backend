package com.example.Taxi.controller;

import com.example.Taxi.domain.Member;
import com.example.Taxi.domain.Post;
import com.example.Taxi.service.GroupService;
import com.example.Taxi.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


/**
 * accessToken의 유효성 검증이 필요하다!!
 */

@Slf4j
@RestController
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final MemberService memberService;

    @PostMapping("/group/new")
    public ResponseEntity<Object> createGroup(@RequestBody @Valid GroupRequestDto groupReqDto) throws Exception {
        Member member = memberService.findMemberByAccessToken(groupReqDto.getAccessToken());
        if (member.getGroup() == null) {
            groupService.createGroup(groupReqDto, member);
            log.info("방 정보" + member.getGroup().getDestination());
            return new ResponseEntity("방 생성 성공!!", HttpStatus.OK);
        } else {
            //@ExceptionHandler @ControllerAdvice 공부후 예외처리 적용
            throw new Exception("이미 입장하거나 생성한 그룹이 존재합니다.");
        }
    }

    // /all 또 /{id}를 추가하여 특정
    @GetMapping("/group")
    public List<GroupResponseDto> findGroup() {
        return groupService.findGroups();
    }

    @GetMapping("/group/{accessToken}")
    public GroupResponseDto findMyGroup(@PathVariable String accessToken) throws Exception {
        return groupService.findMyGroup(accessToken);
    }

    @PostMapping("/group/{id}")
    public void enterGroup(@PathVariable Long id, @RequestBody TokenDto tokenDto) throws Exception {
        log.info(tokenDto.getAccessToken());
        groupService.enter(id, tokenDto.getAccessToken());
    }

    @PostMapping("/group/{id}/exit")
    public void exitGroup(@PathVariable Long id, @RequestBody TokenDto tokenDto) throws Exception{
        Member member = memberService.findMemberByAccessToken(tokenDto.getAccessToken());
        groupService.exit(id, tokenDto.getAccessToken());
    }

    @GetMapping("/group/{id}/post")
    public List<PostResDto> getPost(@PathVariable Long id) throws Exception {
        List<PostResDto> postResDtos = new ArrayList<>();
        for(Post post : groupService.findOne(id).getPosts()){
            postResDtos.add(new PostResDto(post));
        }
        return postResDtos;
    }
}
