package com.example.Taxi.member;

import com.example.Taxi.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepo {

    private final EntityManager em;

    public void save(Member member) {
        List<Member> findMembers = findMemberByIdentityNum(member.getIdentityNum());
        if (findMembers.isEmpty()) {
            em.persist(member);
        } else {
            findMembers.get(0).updateTokens(member.getAccessTokenKaKao(), member.getRefreshTokenKaKao());
        }

    }

    public List<Member> findMemberByIdentityNum(Long identityNum) {
        List<Member> members = em.createQuery("select m from Member m where m.identityNum = :identityNum", Member.class)
                .setParameter("identityNum", identityNum)
                .getResultList();
        return members;
    }

    public void remove(Member member){
        em.remove(member);
    }
}

