package com.example.Taxi.room;

import com.example.Taxi.config.exception.CustomException;
import com.example.Taxi.config.exception.CustomExceptionStatus;
import com.example.Taxi.item.JpaItemRepo;
import com.example.Taxi.member.Member;
import com.example.Taxi.member.MemberRepo;
import com.example.Taxi.token.TokenDto;
import com.example.Taxi.token.TokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepo;
    private final TokenRepo tokenRepo;
    private final MemberRepo memberRepo;

    public List<RoomResDto> findMyRoom(TokenDto token) {
        Member member = findMemberByAccessToken(token.getAccessToken());
        return roomRepo.findAllByMember(member).stream().map(RoomResDto::new).collect(Collectors.toList());
    }

    private Member findMemberByAccessToken(String accessToken) {
        Long identityNum = tokenRepo.findIdentityNumByAccessToken(accessToken)
                .orElseThrow(()->new CustomException(CustomExceptionStatus.INVALID_TOKEN));
        Member member = memberRepo.findMemberByIdentityNum(identityNum)
                .orElseThrow(()->new CustomException(CustomExceptionStatus.INVALID_IDENTITY_NUM));
        return member;
    }
}
