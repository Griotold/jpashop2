package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    // save 또는 update
    public void save(Item item){
        if(item.getId()==null){
            em.persist(item);
        }else{
            em.merge(item);
        }
    }

    // 단건 조회
    public Item findOne(Long id){
        Item item = em.find(Item.class, id);
        return item;
    }

    // 모두 조회
    public List<Item> findAll(){
        List<Item> allItems = em.createQuery("select i from Item i", Item.class)
                .getResultList();
        return allItems;
    }

}
