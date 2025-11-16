package com.foodfast.order_service.service;

import com.foodfast.order_service.model.Order;
import com.foodfast.order_service.model.OrderItem;
import com.foodfast.order_service.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    private Order order;

    @BeforeEach
    void setup() {
        order = Order.builder()
                .fullname("Nguyen Van Test")
                .phone("0901112222")
                .address("123 Street")
                .total(new BigDecimal("200000"))
                .items(List.of(new OrderItem(null, "P001", 2, new BigDecimal("100000"))))
                .status(0)
                .paymethod(1)
                .build();
    }

    @Test
    void testCreateAndRetrieveOrder() {
        Order saved = orderRepository.save(order);
        assertNotNull(saved.getId());

        Order fetched = orderRepository.findById(saved.getId()).orElse(null);
        assertNotNull(fetched);
        assertEquals("Nguyen Van Test", fetched.getFullname());
        assertEquals(1, fetched.getItems().size());
    }

    @Test
    void testUpdateOrderStatus() {
        Order saved = orderRepository.save(order);
        saved.setStatus(1); // confirmed
        orderRepository.save(saved);

        Order updated = orderRepository.findById(saved.getId()).get();
        assertEquals(1, updated.getStatus());
    }
}
