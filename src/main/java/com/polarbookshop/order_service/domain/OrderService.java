package com.polarbookshop.order_service.domain;

import com.polarbookshop.order_service.book.Book;
import com.polarbookshop.order_service.book.BookClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final BookClient bookClient;

    public OrderService(OrderRepository orderRepository, BookClient bookClient) {
        this.orderRepository = orderRepository;
        this.bookClient = bookClient;
    }

    public Flux<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Mono<Order> submitOrder(String isbn, int quantity) {
        return bookClient.getBookByIsbn(isbn)
                .map(book -> buildAcceptedOrder(book, quantity))
                .defaultIfEmpty(buildRejectOrder(isbn, quantity))
                .flatMap(orderRepository::save);
    }

    private Order buildAcceptedOrder(Book book, int qty) {
        return Order.of(
                book.isbn(),
                book.title(),
                book.price(),
                qty,
                OrderStatus.ACCEPTED
        );
    }
    public static Order buildRejectOrder(String bookIsbn, int qty) {
        return Order.of(
                bookIsbn,
                null,
                null,
                qty,
                OrderStatus.REJECTED
        );
    }
}
