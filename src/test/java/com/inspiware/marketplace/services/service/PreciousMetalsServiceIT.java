package com.inspiware.marketplace.services.service;

import com.inspiware.marketplace.services.MarketPlacePreciousMetalApp;
import com.inspiware.marketplace.services.domain.Order;
import com.inspiware.marketplace.services.domain.OrderSummary;
import com.inspiware.marketplace.services.domain.OrderType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.junit.Assert.*;
import static com.inspiware.marketplace.services.utils.TestUtils.getAnotherOrder;
import static com.inspiware.marketplace.services.utils.TestUtils.getAllOrders;

@ActiveProfiles({"service", "test"})
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MarketPlacePreciousMetalApp.class)
public class PreciousMetalsServiceIT {

    @Autowired
    private PreciousMetalsService service;

    @Before
    public void initTests(){
        getAllOrders().forEach(o -> service.registerOrder(o));
    }

     @Test
    public void test_find_by_user_id() {
        Set<Order> orders = service.findAllByUserId("TEST_USER_1");
        assertFalse(orders.isEmpty());
        assertEquals(6, orders.size());

        orders = service.findAllByUserId("TEST_USER_2");
        assertFalse(orders.isEmpty());
        assertEquals(4, orders.size());

        orders = service.findAllByUserId("TEST_USER_3");
        assertFalse(orders.isEmpty());
        assertEquals(5, orders.size());
    }

    @Test
    public void test_find_by_unknown_user_id() {
        Set<Order> orders = service.findAllByUserId("TEST_USER");
        assertTrue(orders.isEmpty());
    }

    @Test
    public void test_buy_order_summary() {
        OrderSummary[] orderSummaries = service.orderSummary(OrderType.BUY);
        assertEquals(5, orderSummaries.length);
        assertEquals(10.40, orderSummaries[0].getQuantity().doubleValue(), 0.00);
        assertEquals(3.60, orderSummaries[3].getQuantity().doubleValue(), 0.00);
        assertEquals(307, orderSummaries[3].getPricePerKg().doubleValue(), 0.00);
    }

    @Test
    public void test_register_and_cancellation_of_an_order() {
        Order order = getAnotherOrder();

        service.registerOrder(order);

        OrderSummary[] orderSummaries = service.orderSummary(OrderType.BUY);
        assertEquals(5, orderSummaries.length);
        assertEquals(13.70, orderSummaries[0].getQuantity().doubleValue(), 0.00);
        assertEquals(314, orderSummaries[0].getPricePerKg().doubleValue(), 0.00);

        service.cancelOrder(order.getId(), order.getUserId());

        orderSummaries = service.orderSummary(OrderType.BUY);
        assertEquals(5, orderSummaries.length);
        assertEquals(10.40, orderSummaries[0].getQuantity().doubleValue(), 0.00);
        assertEquals(314, orderSummaries[0].getPricePerKg().doubleValue(), 0.00);
    }
}
