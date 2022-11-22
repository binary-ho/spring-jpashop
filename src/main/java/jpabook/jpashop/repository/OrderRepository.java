package jpabook.jpashop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.QMember;
import jpabook.jpashop.domain.QOrder;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

//    public List<Order> findAll(OrderSearch orderSearch) {
//        return em.createQuery("select o from Order o", Order.class)
//            .getResultList();
//    }

    public List<Order> findAll(OrderSearch orderSearch) {
        QOrder order = QOrder.order;
        QMember member = QMember.member;

        return query.select(order)
            .from(order)
            .join(order.member, member)
            .where(statusEqual(orderSearch.getOrderStatus()), nameLike(orderSearch.getMemberName()))
            .limit(1000)
            .fetch();
    }

    private BooleanExpression nameLike(String memberName) {
        if (!StringUtils.hasText(memberName)) return null;
        return QMember.member.name.like(memberName);
    }

    private BooleanExpression statusEqual(OrderStatus statusCondition) {
        if (statusCondition == null) return null;
        return QOrder.order.status.eq(statusCondition);
    }

    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order as o join fetch o.member m join fetch o.delivery d", Order.class)
            .getResultList();
    }

    // XToOne 관계는 아무리 fetch join 해도 문제가 없어
    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                "select o from Order as o"
                    + " join fetch o.member m"
                    + " join fetch o.delivery d", Order.class)
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
    }

    public List<Order> findAllWithItem() {
        return em.createQuery(
                "select distinct o from Order o"
                    + " join fetch o.member m"
                    + " join fetch o.delivery d"
                    + " join fetch o.orderItems oi"
                    + " join fetch oi.item i", Order.class)
            .getResultList();
    }



//    public List<Order> findAll(OrderSearch orderSearch) {
//        return em.createQuery("select o from Order as o join o.member as m " +
//                        "where o.status = :status " +
//                        "and m.name like :name", Order.class)
//                .setParameter("status", orderSearch.getOrderStatus())
//                .setParameter("name", orderSearch.getMemberName())
//                .setFirstResult(100)
//                .setMaxResults(1000)
//                .getResultList();
//    }
}
