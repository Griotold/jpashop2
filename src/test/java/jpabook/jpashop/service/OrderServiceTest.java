package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문()throws Exception {
        // given
//        Address address = new Address("인천시", "영성동로", "12345");
        Member member = createMember();

        Book book = createBook("JPA",10000, 20);

        int orderCount = 5;
        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        Order getOrder = orderRepository.findOne(orderId);

        // then
        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다."
                , 1,getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다."
                , book.getPrice() * orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다.", 15, book.getStockQuantity());

    }

    @Test
    public void 주문취소()throws Exception {
        // given
        Member member = createMember();

        Book book = createBook("JPA",10000, 20);

        int orderCount = 5;
        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        orderService.cancelOrder(orderId);
        Order getOrder = orderRepository.findOne(orderId);

        // then
        assertEquals("상품 주문 취소시 상태는 CANCEL"
                , OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("주문 취소시 재고는 원상복구 되어야 한다."
                , 20, book.getStockQuantity());


    }



    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과()throws Exception {
        // given
        Member member = createMember();
        Book book = createBook("JPA",10000, 20);

        // when
        orderService.order(member.getId(), book.getId(), 25);
        // then
        fail("예외가 발생해야 한다.");
    }
    private Book createBook(String bookName, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(bookName);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        book.setAuthor("김영한");
        book.setIsbn("123456789");
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("griotold");
        member.setAddress(new Address("인천시", "영성동로", "12345"));
        em.persist(member);
        return member;
    }
}