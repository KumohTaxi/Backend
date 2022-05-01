package com.example.Taxi.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Post {
    @Id @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "member_id")
    private Member member;
    private String msg;
    private LocalDateTime postTime;

    @Builder
    public Post(Member member, String msg, LocalDateTime postTime) {
        this.member = member;
        this.msg = msg;
        this.postTime = postTime;
    }
}
