package com.example.Taxi.group;

import com.example.Taxi.config.exception.CustomException;
import com.example.Taxi.config.exception.CustomExceptionStatus;
import com.example.Taxi.member.Gender;
import com.example.Taxi.token.TokenDto;
import com.example.Taxi.member.Member;
import com.example.Taxi.member.MemberRepo;
import com.example.Taxi.token.TokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO 세션 관련 로직 깔끔하게 다시 짜기(orElseThrow..?, Optional...?) or Spring Security 구현
 */

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

    @Transactional
    public List<GroupResponseDto> findGroups(TokenDto tokenDto) {

        List<GroupResponseDto> groupResDtos =new ArrayList<>();
        Member member = findMemberByAccessToken(tokenDto.getAccessToken());

        for (Group group : groupRepo.findAll()) {
            if(isValidateGroup(group,member)) groupResDtos.add(new GroupResponseDto(group));
        }
        return groupResDtos;
    }

    public Group findOne(Long id) {

        Group group = groupRepo.findById(id);
        if (group == null) throw new CustomException(CustomExceptionStatus.NOT_EXIST_GROUP);

        return group;
    }

    @Transactional
    public Long enter(Long id, String accessToken) {

        Group group = groupRepo.findById(id);
        Member member = findMemberByAccessToken(accessToken);
        group.involveMember(member);
        return group.getId();
    }

    @Transactional
    public void exit(Long id, String accessToken) {

        Member member = findMemberByAccessToken(accessToken);
        Group group = groupRepo.findById(id);
        group.removeMember(member);
    }

    public GroupResponseDto findMyGroup(String accessToken) {

        Member member = findMemberByAccessToken(accessToken);
        if (member.getGroup() == null) {
            throw new CustomException(CustomExceptionStatus.NOT_EXIST_GROUP);
        } else if (member.getGroup().getDateTime().isBefore(LocalDateTime.now().minusHours(1))) {
            member.exitGroup();
            throw new CustomException(CustomExceptionStatus.NOT_EXIST_GROUP);
        }
        return new GroupResponseDto(member.getGroup());
    }

    public List<GroupResponseDto> findGroupsByDst(String destination, TokenDto tokenDto) {

        List<GroupResponseDto> groupResDtos =new ArrayList<>();
        Member member = findMemberByAccessToken(tokenDto.getAccessToken());

        for (Group group : groupRepo.findAll()) {
            if(isValidateGroup(group,member) &&
                    group.getDestination().equals(destination)) groupResDtos.add(new GroupResponseDto(group));
        }
        return groupResDtos;

    }

    /**
     * TODO 그룹 유효성 검증에 대한 테스트 코드 작성하기
     */
    private boolean isValidateGroup(Group group, Member member) {

        if(group.getMembers().size() == 0);
        else if(group.getDateTime().isBefore(LocalDateTime.now().minusHours(1))) group.removeAllMember();
        else return member.getGender().equals(Gender.ALL) ||
                    member.getGender().equals(group.getMembers().get(0).getGender());

        return false;
    }

    private Member findMemberByAccessToken(String accessToken) {
        Long identityNum = tokenRepo.findIdentityNumByAccessToken(accessToken)
                .orElseThrow(()->new CustomException(CustomExceptionStatus.INVALID_TOKEN));
        Member member = memberRepo.findMemberByIdentityNum(identityNum)
                .orElseThrow(()->new CustomException(CustomExceptionStatus.INVALID_IDENTITY_NUM));
        return member;
    }
}
