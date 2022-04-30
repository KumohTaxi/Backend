package com.example.Taxi.controller;

import com.example.Taxi.JwtTokenProvider;
import com.example.Taxi.domain.Group;
import com.example.Taxi.domain.Member;
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

@Slf4j
@RestController
@RequiredArgsConstructor
public class GroupController {
    
    private final GroupService groupService;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/group/new")
    public ResponseEntity<Object> createGroup(@RequestBody @Valid GroupRequestDto groupRequestDto) throws Exception {
        if(jwtTokenProvider.validateToken(groupRequestDto.getAccessToken())){
            Long identityNumForMember = jwtTokenProvider.getIdentityNumByAccessToken(groupRequestDto.getAccessToken());
            Member member = memberService.findMemberByIdentityNum(identityNumForMember);
            if (member.getGroups() == null){
                groupService.createGroup(groupRequestDto.toEntity(member));
                log.info("방 정보" + member.getGroups().getDestination());
                return new ResponseEntity("방 생성 성공!!", HttpStatus.OK);
            } else{
                //@ExceptionHandler @ControllerAdvice 공부후 예외처리 적용
                throw new Exception("이미 입장하거나 생성한 그룹이 존재합니다.");
            }
        } else{
            //이것도 추후에 예외처리로 처리하기
            return new ResponseEntity("세션정보가 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }



    }

    // /all 또 /{id}를 추가하여 특정
    @GetMapping("/group")
    public List<GroupResponseDto> findGroup() {
        List<Group> groups = groupService.findGroups();
        List<GroupResponseDto> groupResponseDtos = new ArrayList<>();
        for (Group group : groups) {
            groupResponseDtos.add(new GroupResponseDto(group));
        }
        return groupResponseDtos;
    }
}
