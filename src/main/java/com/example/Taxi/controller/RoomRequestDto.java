package com.example.Taxi.controller;


import com.example.Taxi.domain.Room;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RoomRequestDto {

    private String destination;
    private LocalDateTime dateTime;
    private Double latitude; //위도
    private Double longitude; //경도

    @Builder
    public RoomRequestDto(String destination, LocalDateTime dateTime, Double latitude, Double longitude) {
        this.destination = destination;
        this.dateTime = dateTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Room toEntity() {
        return Room.builder()
                .destination(destination)
                .dateTime(dateTime)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
