package com.example.Taxi.member;

import com.example.Taxi.config.exception.CustomException;
import com.example.Taxi.config.exception.CustomExceptionStatus;
import com.example.Taxi.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.awt.*;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepo {

    private final EntityManager em;

    public void save(Member member) {
        Optional<Member> findMember = findMemberByIdentityNum(member.getIdentityNum());
        if (!findMember.isPresent()) {
            em.persist(member);
        } else {
            findMember.get().updateTokens(member.getAccessTokenKaKao(), member.getRefreshTokenKaKao());
        }

    }

    public Optional<Member> findMemberByIdentityNum(Long identityNum) {
        return em.createQuery("select m from Member m where m.identityNum = :identityNum", Member.class)
                .setParameter("identityNum", identityNum)
                .getResultList()
                .stream().findFirst();

    }

    public void remove(Member member){
        em.remove(member);
    }
}

