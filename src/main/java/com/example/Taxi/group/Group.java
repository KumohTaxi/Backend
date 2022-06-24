package com.example.Taxi.group;

import com.example.Taxi.member.Member;
import com.example.Taxi.post.Post;
import com.example.Taxi.member.Status;
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

    @OneToMany(mappedBy = "group")
    private List<Post> posts = new ArrayList<>();

    @Builder
    public Group(String destination, LocalDateTime dateTime, Double latitude, Double longitude, Member member) {
        this.destination = destination;
        this.dateTime = dateTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.members.add(member);
        member.makeGroup(this);
    }

    public void removeMember(Member member) {
        members.remove(member);
        member.exitGroup();
        if(member.getStatus() == Status.CAPTAIN && members.size() > 0){
            members.get(0).promoteStatus();
        }
    }

    public void involveMember(Member member) throws Exception{
        if(members.get(0).getGender() != member.getGender()){
            throw new Exception("방 성별과 입장할 사용자의 성별이 다릅니다.");
        } else{
            members.add(member);
            member.joinGroup(this);
        }
    }

    public void post(Post post) {
        posts.add(post);
    }

    public void removeAllMember() {
        for (Member member : members) {
            member.exitGroup();
        }
        members.clear();
    }
}
