package org.example.petstore.builders;

import org.example.petstore.data.Order;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

public class OrderBuilder {
    private static final AtomicLong idGenerator = new AtomicLong(1);

    public static Order buildDefaultOrder() {
        return Order.builder()
                .id(idGenerator.getAndIncrement())
                .petId(1L)
                .quantity(1)
                .shipDate(LocalDateTime.now().plusDays(1)
                        .format(DateTimeFormatter.ISO_DATE_TIME))
                .status("placed")
                .complete(false)
                .build();
    }

    public static Order buildWithParams(Long petId, Integer quantity, String status) {
        return Order.builder()
                .id(idGenerator.getAndIncrement())
                .petId(petId)
                .quantity(quantity)
                .shipDate(LocalDateTime.now().plusDays(2)
                        .format(DateTimeFormatter.ISO_DATE_TIME))
                .status(status)
                .complete(false)
                .build();
    }
}
