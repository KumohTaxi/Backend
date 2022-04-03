package com.example.Taxi.controller;

import com.example.Taxi.domain.Room;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class RoomResponseDto {
    private String destination;
    private LocalDateTime dateTime;
    private Double latitude;
    private Double longitude;

    public RoomResponseDto(Room room) {
        destination = room.getDestination();
        dateTime = room.getDateTime();
        latitude = room.getLatitude();
        longitude = room.getLongitude();
    }
}
