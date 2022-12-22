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

    //==생성 메소드==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.ORDER);
        return order;
    }

    //==비즈니스 로직==//
    /**
     * 주문 취소
     * */
    public void cancel(){
        // 검증
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송된 상품은 취소가 불가능합니다.");
        }
        // 주문 상태를 취소로
        this.setStatus(OrderStatus.CANCEL);
        // orderItems를 각각 취소
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }
    /**
     * 전체 주문 가격 조회
     * */
    public int getTotalPrice() {
        int total = 0;
        for (OrderItem orderItem : orderItems) {
            total += orderItem.getTotalPrice();
        }
        return total;
    }
    // 스트림 문법의 경우
//    public int getTotalPrice() {
//        int totalPrice = orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
//        return totalPrice;
//    }
}
