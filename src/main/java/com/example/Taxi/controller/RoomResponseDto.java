package com.example.Taxi.controller;

import com.example.Taxi.domain.Room;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RoomResponseDto {
    private String destination;
    private LocalDateTime dateTime;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public RoomResponseDto(Room room) {
        destination = room.getDestination();
        dateTime = room.getDateTime();
        latitude = room.getLatitude();
        longitude = room.getLongitude();
    }
}
