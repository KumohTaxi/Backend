package com.example.Taxi.group;

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
        return em.createQuery("select g from Group g", Group.class)
                .getResultList();
    }

    public Group findById(Long id) {
        return em.find(Group.class, id);
    }
}
