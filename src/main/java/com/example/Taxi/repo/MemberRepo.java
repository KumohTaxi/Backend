package com.example.Taxi.repo;

import com.example.Taxi.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepo {

    private final EntityManager em;

    public List<Member> findByAccessToken(String accessToken) {
        return em.createQuery("select m from Member m where m.accessTokenKaKao = :accessToken")
                .setParameter("accessToken", accessToken)
                .getResultList();
    }

    public void save(Member member) {
        List<Member> findMembers = findByNickname(member.getNickname());
        if (findMembers.isEmpty()) {
            em.persist(member);
        } else {
            findMembers.get(0).updateTokens(member.getAccessTokenKaKao(), member.getRefreshTokenKaKao());
        }

    }

    public List<Member> findByNickname(String nickname) {
        List<Member> members = em.createQuery("select m from Member m where m.nickname = :nickname", Member.class)
                .setParameter("nickname", nickname)
                .getResultList();
        return members;
    }

    public List<Member> findMemberByIdentityNum(Long identityNum) {
        List<Member> members = em.createQuery("select m from Member m where m.identityNum = :identityNum", Member.class)
                .setParameter("identityNum", identityNum)
                .getResultList();
        return members;
    }
}

