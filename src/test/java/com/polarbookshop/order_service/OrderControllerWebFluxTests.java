package com.polarbookshop.order_service;

import com.polarbookshop.order_service.domain.Order;
import com.polarbookshop.order_service.domain.OrderController;
import com.polarbookshop.order_service.domain.OrderService;
import com.polarbookshop.order_service.domain.OrderStatus;
import com.polarbookshop.order_service.web.OrderRequest;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

@WebFluxTest(OrderController.class)
public class OrderControllerWebFluxTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private OrderService orderService;

    @Test
    void whenBookNotAvailableThenRejectOrder() {
        var BOOK_ISBN = "1234567890";
        OrderRequest orderRequest = new OrderRequest(BOOK_ISBN, 3);

        // WHEN
        Mono<Order> expectedOrder = orderService.submitOrder(orderRequest.isbn(), orderRequest.quantity());
        BDDMockito.when(orderService.submitOrder(orderRequest.isbn(), orderRequest.quantity()))
                .thenReturn(expectedOrder);

        // THEN
        webTestClient
                .post()
                .uri("/orders")
                .bodyValue(orderRequest)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class)
                .value(actualOrder -> {
                    assertThat(actualOrder).isNotNull();
                    assertThat(actualOrder.status()).isEqualTo(OrderStatus.REJECTED);
                });
    }
}
