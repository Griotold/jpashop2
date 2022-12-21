package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepository {
    @PersistenceContext
    private EntityManager em;

    //save
    public void save(Member member){
        em.persist(member);
    }
    //findOne
    public Member findOne(Long id){
        Member findMember = em.find(Member.class, id);
        return findMember;
    }
    //findAll
    public List<Member> findAll(){
        List<Member> result = em.createQuery("select m from Member m"
                        , Member.class)
                .getResultList();
        return result;
    }
    // findByName
    public List<Member> findByName(String name){
        List<Member> result =
                em.createQuery("select m from Member m where m.name= :name"
                        , Member.class)
                .setParameter("name", name)
                .getResultList();
        return result;
    }


}
