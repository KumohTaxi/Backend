package com.example.Taxi.repo;

import com.example.Taxi.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class PostRepo {

    private final EntityManager em;

    public void save(Post post) {
        em.persist(post);
    }
}
