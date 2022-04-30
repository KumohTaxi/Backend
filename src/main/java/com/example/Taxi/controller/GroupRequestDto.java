package com.example.Taxi.controller;


import com.example.Taxi.domain.Group;
import com.example.Taxi.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GroupRequestDto {

    @NotEmpty(message = "로그인이 되어있지 않습니다.")
    private String accessToken;
    private String destination;
    private LocalDateTime dateTime;
    private Double latitude; //위도
    private Double longitude; //경도
    private Member member;

    @Builder
    public GroupRequestDto(String accessToken, String destination, LocalDateTime dateTime, Double latitude, Double longitude) {
        this.accessToken = accessToken;
        this.destination = destination;
        this.dateTime = dateTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Group toEntity(Member member) {
        return Group.builder()
                .destination(destination)
                .dateTime(dateTime)
                .latitude(latitude)
                .longitude(longitude)
                .member(member)
                .build();
    }
}
