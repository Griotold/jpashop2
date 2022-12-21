package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class OrderItem {
    @Id
    @GeneratedValue
    @Column(name="order_item_id")
    private Long id;

    @ManyToOne(fetch= LAZY)
    @JoinColumn(name="item_id") // 연관관계 주인
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="order_id") // 연관관계 주인
    private Order order;

    private int orderPrice;

    private int count;
}
