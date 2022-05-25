package com.example.Taxi.post;

import com.example.Taxi.token.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TokenRepo {
    private final EntityManager em;

    public void save(Token token) {
        em.persist(token);
    }

    public Optional<Long> findIdentityNumByAccessToken(String accessToken) {
        return em.createQuery("select t.identityNum from Token t where t.accessToken = :accessToken")
                .setParameter("accessToken",accessToken)
                .getResultList()
                .stream().findFirst();
    }

    public Optional<Token> findTokenByAccessToken(String accessToken){
        return em.createQuery("select t from Token t where t.accessToken = :accessToken")
                .setParameter("accessToken",accessToken)
                .getResultList()
                .stream().findFirst();
    }

    public Optional<Token> findTokenByIdentityNum(Long identityNum) {
        return em.createQuery("select t from Token t where t.identityNum = :identityNum")
                .setParameter("identityNum",identityNum)
                .getResultList()
                .stream().findFirst();
    }

    public void remove(Token token) {
        em.remove(token);
    }
}
