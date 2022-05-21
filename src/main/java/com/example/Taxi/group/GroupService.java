package com.example.Taxi.group;

import com.example.Taxi.token.TokenDto;
import com.example.Taxi.member.Member;
import com.example.Taxi.token.Token;
import com.example.Taxi.member.MemberRepo;
import com.example.Taxi.post.TokenRepo;
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

    public List<GroupResponseDto> findGroups(TokenDto tokenDto) {
        List<GroupResponseDto> groupResDtos =new ArrayList<>();
        Member member = memberRepo.findMemberByIdentityNum(
                tokenRepo.findTokenByAccessToken(tokenDto.getAccessToken()).get(0).getIdentityNum()).get(0);

        for (Group group : groupRepo.findAll()) {
            if(group.getDateTime().isBefore(LocalDateTime.now().minusHours(1))
                    || group.getMembers().size() == 0 || member.getGender() != group.getMembers().get(0).getGender()){
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
    public void enter(Long id, String accessToken) throws Exception{
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
