package com.example.Taxi.controller.room;


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
    private BigDecimal latitude; //위도
    private BigDecimal longitude; //경도

    @Builder
    public RoomRequestDto(String destination, LocalDateTime dateTime, BigDecimal latitude, BigDecimal longitude) {
        this.destination = destination;
        this.dateTime = dateTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
