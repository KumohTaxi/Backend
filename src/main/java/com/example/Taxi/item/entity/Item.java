package com.example.Taxi.item.entity;

import com.example.Taxi.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@Table(indexes = @Index(name = "idx_atcId",columnList = "atcId"))
@NoArgsConstructor
public class Item {
    @Id @GeneratedValue
    private Long id;
    private String atcId;
    private String location;
    private String name;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String category;
    private LocalDate lostTime;
    private Integer foundOrder;
    private String img;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "member_id")
    private Member registrant;

    @Builder
    public Item(String atcId,String location, String name, Status status,
                String category, LocalDate lostTime, String img, Integer foundOrder) {
        this.atcId = atcId;
        this.location = location;
        this.name = name;
        this.status = status;
        this.category = category;
        this.lostTime = lostTime;
        this.img = img;
        this.foundOrder = foundOrder;
    }

    public void enrollRegistrant(Member member) {
        this.registrant = member;
    }
}
