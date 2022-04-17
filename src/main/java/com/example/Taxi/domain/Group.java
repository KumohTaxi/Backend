package com.example.Taxi.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Group {
    @Id @GeneratedValue
    private Long id;
    private String destination;
    private LocalDateTime dateTime;
    private Double latitude; //위도
    private Double longitude; //경도
    private List<Member> members;

    @Builder
    public Group(String destination, LocalDateTime dateTime, Double latitude, Double longitude, List<Member> members) {
        this.destination = destination;
        this.dateTime = dateTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.members = members;
    }
}
