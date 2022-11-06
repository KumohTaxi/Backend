package com.example.Taxi.room;

import com.example.Taxi.item.entity.Item;
import com.example.Taxi.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Room {
    @Id @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "member_id")
    private Member requester;
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "item_id")
    private Item item;
    private LocalDateTime requestTime;

    public Room(Member member, Item item) {
        this.requester = member;
        this.item = item;
        this.status = Status.WAIT;
        this.requestTime = LocalDateTime.now();
    }

    public void acceptReq() {
        this.status = Status.ACCEPT;
    }
}
