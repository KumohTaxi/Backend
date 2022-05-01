package com.example.Taxi.service;

import com.example.Taxi.controller.GroupRequestDto;
import com.example.Taxi.controller.GroupResponseDto;
import com.example.Taxi.domain.Group;
import com.example.Taxi.domain.Member;
import com.example.Taxi.domain.Token;
import com.example.Taxi.repo.GroupRepo;
import com.example.Taxi.repo.MemberRepo;
import com.example.Taxi.repo.TokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {
    private final GroupRepo groupRepo;
    private final TokenRepo tokenRepo;
    private final MemberRepo memberRepo;

    @Transactional
    public void createGroup(GroupRequestDto groupReqDto, Member member) {
        groupRepo.save(groupReqDto.toEntity(member));
    }

    public List<GroupResponseDto> findGroups() {
        List<GroupResponseDto> groupResDtos =new ArrayList<>();
        for (Group group : groupRepo.findAll()) {
            if(group.getDateTime().isBefore(LocalDateTime.now()) || group.getMembers().size() == 0){
                continue;
            }
            groupResDtos.add(new GroupResponseDto(group));
        }
        return groupResDtos;
    }

    public Group findOne(Long id) throws Exception {
        Group group = groupRepo.findById(id);
        if (group == null) {
            new Exception("존재하지 않습니다");
        }
        return group;
    }

    @Transactional
    public void enter(Long id, String accessToken) {
        Group group = groupRepo.findById(id);
        Token token = tokenRepo.findTokenByAccessToken(accessToken).get(0);
        group.involveMember(memberRepo.findMemberByIdentityNum(token.getIdentityNum()).get(0));
    }

    @Transactional
    public void exit(Long id, String accessToken) {

        Member member = memberRepo.findMemberByIdentityNum(
                tokenRepo.findTokenByAccessToken(accessToken).get(0).getIdentityNum()).get(0);
        Group group = groupRepo.findById(id);
        group.removeMember(member);
    }

    public GroupResponseDto findMyGroup(String accessToken) throws Exception {
        Member member = memberRepo.findMemberByIdentityNum(
                tokenRepo.findTokenByAccessToken(accessToken).get(0).getIdentityNum()).get(0);

        if (member.getGroup() == null) {
            throw new Exception("속한 그룹이 존재하지 않습니다.");
        }
        return new GroupResponseDto(member.getGroup());
    }
}
