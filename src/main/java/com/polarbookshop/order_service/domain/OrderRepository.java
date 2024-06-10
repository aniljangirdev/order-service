package com.polarbookshop.order_service.domain;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {
}
