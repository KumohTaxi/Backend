package com.example.Taxi.controller;

import com.example.Taxi.domain.Group;
import com.example.Taxi.domain.Member;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GroupResponseDto {
    private int memberCount;
    private String destination;
    private LocalDateTime dateTime;
    private Double latitude;
    private Double longitude;

    public GroupResponseDto(Group group, int cnt) {
        memberCount = group.getMembers().size();
        destination = group.getDestination();
        dateTime = group.getDateTime();
        latitude = group.getLatitude();
        longitude = group.getLongitude();
    }
}
