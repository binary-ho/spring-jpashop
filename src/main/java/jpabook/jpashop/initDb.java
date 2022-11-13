package jpabook.jpashop;


import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class initDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit1() {
            Member member = createMember("Jinho", new Address("서울", "7", "7777"));
            em.persist(member);

            Book book = createBook("인생은 아름다워", 10000, 7);
            em.persist(book);

            Book book1 = createBook("라따뚜이", 20000, 10);
            em.persist(book1);

            OrderItem orderItem = OrderItem.createOrderItem(book, book.getPrice(), 1);
            OrderItem orderItem1 = OrderItem.createOrderItem(book1, book1.getPrice(), 2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem, orderItem1);
            em.persist(order);
        }

        public void dbInit2() {
            Member member = createMember("SungJun", new Address("서울", "1", "1111"));
            em.persist(member);

            Book book = createBook("위대한 쇼맨", 10000, 7);
            em.persist(book);

            Book book1 = createBook("로건", 20000, 10);
            em.persist(book1);

            OrderItem orderItem = OrderItem.createOrderItem(book, book.getPrice(), 1);
            OrderItem orderItem1 = OrderItem.createOrderItem(book1, book1.getPrice(), 2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem, orderItem1);
            em.persist(order);
        }

        private Member createMember(String name, Address address) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(address);
            return member;
        }

        private Book createBook(String name, int price, int quantity) {
            Book book = new Book();
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(quantity);
            return book;
        }
    }
}
