package com.foodfast.order_service.service;

import com.foodfast.order_service.model.Order;
import com.foodfast.order_service.model.OrderItem;
import com.foodfast.order_service.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @Transactional
    void testCreateAndRetrieveOrder() {
        Order order = Order.builder()
                .userId("user1")
                .fullname("Nguyen Van A")
                .phone("0123456789")
                .address("123 Street")
                .total(BigDecimal.valueOf(200))
                .status(0)
                .build();

        Order savedOrder = orderService.createOrder(order);
        assertNotNull(savedOrder.getId());

        Order retrieved = orderService.getOrderById(savedOrder.getId()).orElse(null);
        assertNotNull(retrieved);
        assertEquals("Nguyen Van A", retrieved.getFullname());
    }

    @Test
    @Transactional
    void testUpdateOrderStatus() {
        Order order = Order.builder()
                .userId("user2")
                .fullname("Nguyen Van B")
                .total(BigDecimal.valueOf(300))
                .status(0)
                .build();

        order = orderService.createOrder(order);
        Order updated = orderService.updateOrderStatus(order.getId(), 2);

        assertNotNull(updated);
        assertEquals(2, updated.getStatus());
    }

    @Test
    @Transactional
    void testOrderItems() {
        OrderItem item1 = OrderItem.builder()
                .idProduct("p1")
                .quantity(2)
                .price(BigDecimal.valueOf(50))
                .build();

        Order order = Order.builder()
                .userId("user3")
                .fullname("Nguyen Van C")
                .total(BigDecimal.valueOf(100))
                .items(List.of(item1))
                .status(0)
                .build();

        order = orderService.createOrder(order);
        Order saved = orderService.getOrderById(order.getId()).orElse(null);

        assertNotNull(saved);
        assertNotNull(saved.getItems());
        assertEquals(1, saved.getItems().size());
        assertEquals("p1", saved.getItems().get(0).getIdProduct());
    }
}
