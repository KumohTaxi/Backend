package com.example.Taxi.group;

import com.example.Taxi.config.exception.CustomException;
import com.example.Taxi.config.exception.CustomExceptionStatus;
import com.example.Taxi.token.TokenDto;
import com.example.Taxi.member.Member;
import com.example.Taxi.token.Token;
import com.example.Taxi.member.MemberRepo;
import com.example.Taxi.post.TokenRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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

    /**
     * TODO 세션 관련 로직 깔끔하게 다시 짜기(orElseThrow..?, Optional...?) or Spring Security 구현
     */
    public List<GroupResponseDto> findGroups(TokenDto tokenDto) {

        List<GroupResponseDto> groupResDtos =new ArrayList<>();
        try {
            Member member = memberRepo.findMemberByIdentityNum(
                    tokenRepo.findTokenByAccessToken(tokenDto.getAccessToken()).get(0).getIdentityNum()).get(0);
            for (Group group : groupRepo.findAll()) {
                if (group.getMembers().size() == 0 || member.getGender() != group.getMembers().get(0).getGender()) {
                    continue;
                } else if (group.getDateTime().isBefore(LocalDateTime.now().minusHours(1))) {
                    for (Member groupMember : group.getMembers()) {
                        groupMember.exitGroup();
                    }
                    continue;
                }
                groupResDtos.add(new GroupResponseDto(group));
            }
        } catch (IndexOutOfBoundsException ex) {
            throw new CustomException(CustomExceptionStatus.EXPIRE_SESSION);
        }
        return groupResDtos;
    }

    public Group findOne(Long id) throws Exception {
        Group group = groupRepo.findById(id);
        if (group == null) {
            throw new CustomException(CustomExceptionStatus.NOT_EXIST_GROUP);
        }
        return group;
    }

    @Transactional
    public void enter(Long id, String accessToken) {
        Group group = groupRepo.findById(id);
        Token token = tokenRepo.findTokenByAccessToken(accessToken).get(0);
        Member member = memberRepo.findMemberByIdentityNum(token.getIdentityNum()).get(0);
        group.involveMember(member);
        log.info("{} 방 입장: {}",id,member.getNickname());
    }

    @Transactional
    public void exit(Long id, String accessToken) {

        Member member = memberRepo.findMemberByIdentityNum(
                tokenRepo.findTokenByAccessToken(accessToken).get(0).getIdentityNum()).get(0);
        Group group = groupRepo.findById(id);
        group.removeMember(member);
    }

    public GroupResponseDto findMyGroup(String accessToken) {

        Member member = memberRepo.findMemberByIdentityNum(
                tokenRepo.findTokenByAccessToken(accessToken).get(0).getIdentityNum()).get(0);

        if (member.getGroup() == null) {
            throw new CustomException(CustomExceptionStatus.NOT_EXIST_GROUP);
        } else if (member.getGroup().getDateTime().isBefore(LocalDateTime.now().minusHours(1))) {
            member.exitGroup();
        }
        return new GroupResponseDto(member.getGroup());
    }
}
