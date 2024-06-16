package com.polarbookshop.order_service.domain;

import java.time.LocalDate;

public record Error<T>(
        T message,
        LocalDate localDate
) {
}
