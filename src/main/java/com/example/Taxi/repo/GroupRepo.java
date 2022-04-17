package com.example.Taxi.repo;

import com.example.Taxi.domain.Group;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GroupRepo {
    private final EntityManager em;

    public void save(Group group){
        em.persist(group);
    }

    public List<Group> findAll(){
        return em.createQuery("select r from Room r", Group.class)
                .getResultList();
    }
}
