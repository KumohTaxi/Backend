package com.example.Taxi.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TokenRepo {
    private final EntityManager em;

    public void save(Token token) {
        em.persist(token);
    }

    public List<Token> findTokenByAccessToken(String accessToken) {
        return em.createQuery("select t from Token t where t.accessToken = :accessToken")
                .setParameter("accessToken",accessToken)
                .getResultList();
    }

    public List<Token> findTokenByIdentityNum(Long identityNum) {
        return em.createQuery("select t from Token t where t.identityNum = :identityNum")
                .setParameter("identityNum",identityNum)
                .getResultList();
    }

    public void remove(Token token) {
        em.remove(token);
    }
}
