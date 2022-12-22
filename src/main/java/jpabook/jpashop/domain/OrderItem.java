package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 세터로 생성하지 말란 의미
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


    //== 생성 메소드 ==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;

    }

    //== 비즈니스 로직==//
    /**
     * 주문 취소가 되면 재고 원복
     * */
    public void cancel() {
        
        getItem().addStock(count);
    }

    public int getTotalPrice() {
        return getOrderPrice() * getCount();

    }
}
