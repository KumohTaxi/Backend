package com.example.Taxi.controller;

import com.example.Taxi.domain.Group;
import com.example.Taxi.domain.Member;
import com.example.Taxi.service.GroupService;
import com.example.Taxi.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GroupController {
    
    private final GroupService groupService;
    private final MemberService memberService;

    private int cnt = 0;

    @PostMapping("/group/new")
    public void createGroup( @RequestBody @Valid GroupRequestDto groupRequestDto) {
        Member member = memberService.findMember(groupRequestDto.getAccessToken());
        groupService.saveGroup(groupRequestDto.toEntity(member));
    }

    @GetMapping("/group")
    public List<GroupResponseDto> findGroup() {
        List<Group> groups = groupService.findGroups();
        List<GroupResponseDto> groupResponseDtos = new ArrayList<>();
        for (Group group : groups) {
            groupResponseDtos.add(new GroupResponseDto(group,cnt));
        }
        return groupResponseDtos;
    }
}
