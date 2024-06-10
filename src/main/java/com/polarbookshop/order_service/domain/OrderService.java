package com.polarbookshop.order_service.domain;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Flux<Order> getAllOrders() {
        return orderRepository.findAll();
    }


    public Mono<Order> submitOrder(String bookIsbn, int qty) {

        Order orderBuilder = buildRejectOrder(bookIsbn, qty);

        Mono<Order> just = Mono.just(orderBuilder);

        return just.flatMap(orderRepository::save);
    }

    private Order buildRejectOrder(String bookIsbn, int qty) {
        return Order.of(
                bookIsbn,
                null,
                null,
                qty,
                OrderStatus.REJECTED
        );
    }
}
