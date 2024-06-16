package com.polarbookshop.order_service;


import com.polarbookshop.order_service.config.DataConfig;
import com.polarbookshop.order_service.domain.OrderRepository;
import com.polarbookshop.order_service.domain.OrderService;
import com.polarbookshop.order_service.domain.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.test.StepVerifier;

@DataR2dbcTest
@Import(DataConfig.class)
@Testcontainers
public class OrderRepositoryR2dbcTests {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

    @Autowired
    private OrderRepository orderRepository;

    @DynamicPropertySource
    private static void postgresqlProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.r2dbc.url", OrderRepositoryR2dbcTests::r2dbcUrl);
        dynamicPropertyRegistry.add("spring.r2dbc.username", postgreSQLContainer::getUsername);
        dynamicPropertyRegistry.add("spring.r2dbc.password", postgreSQLContainer::getPassword);
        dynamicPropertyRegistry.add("spring.flyway.url", postgreSQLContainer::getJdbcUrl);
    }

    private static String r2dbcUrl() {
        //jdbc:postgresql://localhost:5432/polardb_order
        return String.format("r2dbc:postgresql://%s:%s/%s",
                postgreSQLContainer.getHost(),
                postgreSQLContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
                postgreSQLContainer.getDatabaseName());
    }

    @Test
    void createRejectedOrder() {
        // GIVEN
        var bookIsbn = "1234567890";


        // WHEN
        var rejectedOrder = OrderService.buildRejectOrder(bookIsbn, 250);

        // THEN
        StepVerifier
                .create(orderRepository.save(rejectedOrder))
                .expectNextMatches(order -> order.bookIsbn().equals(bookIsbn) && order.status() == OrderStatus.REJECTED)
                .verifyComplete();
    }
}
