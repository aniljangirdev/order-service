package com.polarbookshop.order_service.book;

import java.math.BigDecimal;

public record Book(
        String isbn,
        String title,
        BigDecimal price,
        String author
) {
}
