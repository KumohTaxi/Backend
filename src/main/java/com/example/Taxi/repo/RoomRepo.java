package com.example.Taxi.repo;

import com.example.Taxi.domain.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoomRepo {
    private final EntityManager em;

    public void save(Room room){
        em.persist(room);
    }

    public List<Room> findAll(){
        return em.createQuery("select r from Room r", Room.class)
                .getResultList();
    }
}
