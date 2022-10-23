package com.example.Taxi.room;

import com.example.Taxi.item.entity.Item;
import com.example.Taxi.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoomRepository {

    private final EntityManager em;

    public List<Room> findAllByMember(Member member) {
        return em.createQuery("select r from Room r where r.requester = :requester " +
                        "or r.item.registrant = :registrant")
                .setParameter("requester", member)
                .setParameter("registrant",member)
                .getResultList();
    }
}
