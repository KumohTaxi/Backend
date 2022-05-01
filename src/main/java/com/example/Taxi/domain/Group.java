package com.example.Taxi.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "groups")
@NoArgsConstructor
public class Group {
    @Id @GeneratedValue
    private Long id;
    private String destination;
    private LocalDateTime dateTime;
    private Double latitude; //위도
    private Double longitude; //경도

    @OneToMany(mappedBy = "group")
    private List<Member> members = new ArrayList<>();

    @OneToMany @JoinColumn(name = "post_id")
    private List<Post> posts = new ArrayList<>();

    @Builder
    public Group(String destination, LocalDateTime dateTime, Double latitude, Double longitude, Member member) {
        this.destination = destination;
        this.dateTime = dateTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.members.add(member);
        member.joinGroup(this);
    }
    
    public void removeMember(Member member) {
        members.remove(member);
        member.exitGroup();
    }

    public void involveMember(Member member) {
        members.add(member);
        member.joinGroup(this);
    }

    public void post(Post post) {
        posts.add(post);
    }
}
