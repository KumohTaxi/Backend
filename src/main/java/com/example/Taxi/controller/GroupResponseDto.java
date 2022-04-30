package com.example.Taxi.controller;

import com.example.Taxi.domain.Group;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GroupResponseDto {
    private Long id;
    private int memberCount;
    private String destination;
    private LocalDateTime dateTime;
    private Double latitude;
    private Double longitude;

    public GroupResponseDto(Group group) {
        id = group.getId();
        memberCount = group.getMembers().size();
        destination = group.getDestination();
        dateTime = group.getDateTime();
        latitude = group.getLatitude();
        longitude = group.getLongitude();
    }
}
