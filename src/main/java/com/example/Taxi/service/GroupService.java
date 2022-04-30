package com.example.Taxi.service;

import com.example.Taxi.controller.GroupRequestDto;
import com.example.Taxi.controller.GroupResponseDto;
import com.example.Taxi.domain.Group;
import com.example.Taxi.domain.Member;
import com.example.Taxi.repo.GroupRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {
    private final GroupRepo groupRepo;

    @Transactional
    public void createGroup(GroupRequestDto groupReqDto, Member member) {
        groupRepo.save(groupReqDto.toEntity(member));
    }

    public List<GroupResponseDto> findGroups() {
        List<GroupResponseDto> groupResDtos =new ArrayList<>();
        for (Group group : groupRepo.findAll()) {
            groupResDtos.add(new GroupResponseDto(group));
        }
        return groupResDtos;
    }
}
