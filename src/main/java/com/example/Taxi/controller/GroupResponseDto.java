package com.example.Taxi.controller;

import com.example.Taxi.domain.Group;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GroupResponseDto {
    private String destination;
    private LocalDateTime dateTime;
    private Double latitude;
    private Double longitude;

    public GroupResponseDto(Group group) {
        destination = group.getDestination();
        dateTime = group.getDateTime();
        latitude = group.getLatitude();
        longitude = group.getLongitude();
    }
}
