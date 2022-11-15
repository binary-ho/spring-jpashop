package jpabook.jpashop.repository.order.query;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    // V4: 직접 조회
    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> orders = findOrders();

        // 여기가 N회 쿼리 발생 부분이다 이 말이야.
        orders.forEach(order -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(order.getOrderId());
            order.setOrderItems(orderItems);
        });
        return orders;
    }

    // V5: 컬렉션 조회 최적화
    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> orders = findOrders();

        // order들의 id 빼와서 IN 으로 쿼리 바꾸어 주자
        List<Long> orderIds = toOrderIds(orders);

        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(
            orderIds);   // (물론 inline 해도 됨)

        // 이제 orders에 map 내용 대입하기
        orders.forEach(order -> order.setOrderItems(orderItemMap.get(order.getOrderId())));

        return orders;
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        // 이거 이용해서 쿼리 최적화 합시다.
        List<OrderItemQueryDto> orderItems = em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)"
                    + " from OrderItem oi"
                    + " join oi.item i"
                    + " where oi.order.id in :orderIds", OrderItemQueryDto.class)
            .setParameter("orderIds", orderIds)
            .getResultList();

        // 한번에 가져온 다음 메모리에서 넣어주자는 마인드.
        // key가 getOrderId의 결과가 되고, value는 List이다.
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
            .collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));

        return orderItemMap;
    }

    private List<Long> toOrderIds(List<OrderQueryDto> orders) {
        return orders.stream()
            .map(order -> order.getOrderId())
            .collect(Collectors.toList());
    }


    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)"
                    + " from Order as o"
                    + " join o.member m"
                    + " join o.delivery d", OrderQueryDto.class)
            .getResultList();
    }

    // V4용 N회 쿼리 발생 DTO 직접 조회 부분
    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)"
                    + " from OrderItem oi"
                    + " join oi.item i"
                    + " where oi.order.id = :orderId", OrderItemQueryDto.class)
            .setParameter("orderId", orderId)
            .getResultList();
    }

    /*    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    private String itemName;
    private int orderPrice;
    private int count;
*/
    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)"
                    + " from Order as o"
                    + " join o.member m"
                    + " join o.delivery d"
                    + " join o.orderItems oi "
                    + "join oi.item i", OrderFlatDto.class)
            .getResultList();
    }
}
