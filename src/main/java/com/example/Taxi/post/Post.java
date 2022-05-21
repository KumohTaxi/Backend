package com.example.Taxi.post;

import com.example.Taxi.group.Group;
import com.example.Taxi.member.Member;
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
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "groups_id")
    private Group group;

    @Builder
    public Post(Member member, String msg, LocalDateTime postTime, Group group) {
        this.member = member;
        this.msg = msg;
        this.postTime = postTime;
        this.group = group;
    }
}
