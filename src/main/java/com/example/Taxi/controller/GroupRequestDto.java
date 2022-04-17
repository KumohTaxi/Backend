package com.example.Taxi.controller;


import com.example.Taxi.domain.Group;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GroupRequestDto {

    private String destination;
    private LocalDateTime dateTime;
    private Double latitude; //위도
    private Double longitude; //경도

    @Builder
    public GroupRequestDto(String destination, LocalDateTime dateTime, Double latitude, Double longitude) {
        this.destination = destination;
        this.dateTime = dateTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Group toEntity() {
        return Group.builder()
                .destination(destination)
                .dateTime(dateTime)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
