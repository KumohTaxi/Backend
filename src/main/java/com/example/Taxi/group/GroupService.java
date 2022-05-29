package com.example.Taxi.group;

import com.example.Taxi.config.exception.CustomException;
import com.example.Taxi.config.exception.CustomExceptionStatus;
import com.example.Taxi.token.TokenDto;
import com.example.Taxi.member.Member;
import com.example.Taxi.token.Token;
import com.example.Taxi.member.MemberRepo;
import com.example.Taxi.token.TokenRepo;
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

    /**
     * TODO 세션 관련 로직 깔끔하게 다시 짜기(orElseThrow..?, Optional...?) or Spring Security 구현
     */
    public List<GroupResponseDto> findGroups(TokenDto tokenDto) {

        List<GroupResponseDto> groupResDtos =new ArrayList<>();
        Long identityNum = tokenRepo.findIdentityNumByAccessToken(tokenDto.getAccessToken())
                .orElseThrow(()->new CustomException(CustomExceptionStatus.INVALID_TOKEN));
        Member member = memberRepo.findMemberByIdentityNum(identityNum)
                .orElseThrow(()->new CustomException(CustomExceptionStatus.INVALID_IDENTITY_NUM));

        for (Group group : groupRepo.findAll()) {
            if (isSameGender(group, member)) continue;
            else if (group.getDateTime().isBefore(LocalDateTime.now().minusHours(1))) {
                for (Member groupMember : group.getMembers()) {
                    groupMember.exitGroup();
                }
                continue;
            }
            groupResDtos.add(new GroupResponseDto(group));
        }
        return groupResDtos;
    }

    private boolean isSameGender(Group group, Member member) {
        return group.getMembers().size() == 0 || member.getGender() != group.getMembers().get(0).getGender();
    }

    public Group findOne(Long id) {
        Group group = groupRepo.findById(id);
        if (group == null) throw new CustomException(CustomExceptionStatus.NOT_EXIST_GROUP);

        return group;
    }

    @Transactional
    public void enter(Long id, String accessToken) {
        Group group = groupRepo.findById(id);
        Long identityNum = tokenRepo.findIdentityNumByAccessToken(accessToken)
                .orElseThrow(()->new CustomException(CustomExceptionStatus.INVALID_TOKEN));
        group.involveMember(memberRepo.findMemberByIdentityNum(identityNum)
                .orElseThrow(()->new CustomException(CustomExceptionStatus.INVALID_IDENTITY_NUM)));
    }

    @Transactional
    public void exit(Long id, String accessToken) {
        Long identityNum = tokenRepo.findIdentityNumByAccessToken(accessToken)
                .orElseThrow(()->new CustomException(CustomExceptionStatus.INVALID_TOKEN));
        Member member = memberRepo.findMemberByIdentityNum(identityNum)
                .orElseThrow(()->new CustomException(CustomExceptionStatus.INVALID_IDENTITY_NUM));
        Group group = groupRepo.findById(id);
        group.removeMember(member);
    }

    public GroupResponseDto findMyGroup(String accessToken) {
        Long identityNum = tokenRepo.findIdentityNumByAccessToken(accessToken)
                .orElseThrow(()->new CustomException(CustomExceptionStatus.INVALID_TOKEN));
        Member member = memberRepo.findMemberByIdentityNum(identityNum)
                .orElseThrow(()->new CustomException(CustomExceptionStatus.INVALID_IDENTITY_NUM));

        if (member.getGroup() == null) {
            throw new CustomException(CustomExceptionStatus.NOT_EXIST_GROUP);
        } else if (member.getGroup().getDateTime().isBefore(LocalDateTime.now().minusHours(1))) {
            member.exitGroup();
            throw new CustomException(CustomExceptionStatus.NOT_EXIST_GROUP);
        }
        return new GroupResponseDto(member.getGroup());
    }
}
