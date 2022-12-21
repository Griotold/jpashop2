package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch= LAZY)
    @JoinColumn(name = "member_id") // 연관관계 주인
    private Member member;

    @OneToMany(mappedBy = "order", cascade = ALL) // OrderItem에 있는 order 필드에 의해 매핑된 것이야.
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch=LAZY, cascade = ALL) // order테이블을 엑세스 많이 하므로 FK가 여기에 두기로. 고로...
    @JoinColumn(name="delivery_id") // 연관관계의 주인!
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    //==연관관계 편의 메소드==// -> 양방향 에서는 해주는게 좋다.
    //setMember
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }
    //addOrderItem
    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    //setDelivery
    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }
}
