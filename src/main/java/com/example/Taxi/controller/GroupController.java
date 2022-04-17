package com.example.Taxi.controller;

import com.example.Taxi.domain.Group;
import com.example.Taxi.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GroupController {
    
    private final GroupService groupService;

    @PostMapping("/room/new")
    public void createRoom(@RequestBody GroupRequestDto groupRequestDto) {
        groupService.saveRoom(groupRequestDto.toEntity());
    }

    @GetMapping("/room")
    public List<GroupResponseDto> findRoom() {
        List<Group> groups = groupService.findRooms();
        List<GroupResponseDto> groupResponseDtos = new ArrayList<>();
        for (Group group : groups) {
            groupResponseDtos.add(new GroupResponseDto(group));
        }
        return groupResponseDtos;
    }
}
