package com.example.Taxi.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Room {
    @Id @GeneratedValue
    private Long id;
    private String destination;
    private LocalDateTime dateTime;
    private BigDecimal latitude; //위도
    private BigDecimal longitude; //경도

    @Builder
    public Room(String destination, LocalDateTime dateTime, BigDecimal latitude, BigDecimal longitude) {
        this.destination = destination;
        this.dateTime = dateTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
