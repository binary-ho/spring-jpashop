package jpabook.jpashop.service.query;


import java.util.List;
import java.util.stream.Collectors;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderRepository orderRepository;

    public List<OrderDto> orderV7() {
        List<Order> orders = orderRepository.findAllWithItem();
        return orders.stream()
            .map(OrderDto::new)
            .collect(Collectors.toList());
    }
}
