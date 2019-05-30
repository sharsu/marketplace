package com.inspiware.marketplace.services.store;

import com.inspiware.marketplace.services.domain.Order;
import com.inspiware.marketplace.services.domain.OrderType;
import com.inspiware.marketplace.services.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class PreciousMetalsStoreTest {

    private PreciousMetalsMapStore<UUID, Order> mapStore;

    @Before
    public void setup() {
        mapStore = new PreciousMetalsMapStore<>("test-store");
    }

    @Test
    public void test_the_name() {
        assertEquals("test-store", mapStore.getName());
    }

    @Test
    public void test_empty_set() {
        assertTrue(mapStore.getAll(x -> true).isEmpty());
    }

    @Test
    public void test_get_all() {
        mapStore.registerOrder(TestUtils.testOrder1.getId(), TestUtils.testOrder1);
        mapStore.registerOrder(TestUtils.testOrder2.getId(), TestUtils.testOrder2);
        mapStore.registerOrder(TestUtils.testOrder3.getId(), TestUtils.testOrder3);
        Set<Order> orders = mapStore.getAll(x -> true);
        assertFalse(orders.isEmpty());
        assertEquals(3, orders.size());
    }

    @Test
    public void test_get_by_id() {
        mapStore.registerOrder(TestUtils.testOrder1.getId(), TestUtils.testOrder1);
        mapStore.registerOrder(TestUtils.testOrder2.getId(), TestUtils.testOrder2);
        mapStore.registerOrder(TestUtils.testOrder3.getId(), TestUtils.testOrder3);

        Order order = mapStore.get(TestUtils.testOrder1.getId());
        assertNotNull(order);

        assertEquals(311, order.getPricePerKg().doubleValue(), 0.00);
        assertEquals(3, order.getQuantity().doubleValue(), 0.00);
    }

    @Test
    public void test_get_by_id_and_type() {
        mapStore.registerOrder(TestUtils.testOrder1.getId(), TestUtils.testOrder1);
        mapStore.registerOrder(TestUtils.testOrder2.getId(), TestUtils.testOrder2);
        mapStore.registerOrder(TestUtils.testOrder3.getId(), TestUtils.testOrder3);

        Order order = mapStore.get(TestUtils.testOrder1.getId(), x -> x.getType() == OrderType.BUY);
        assertNotNull(order);

        assertEquals(311, order.getPricePerKg().doubleValue(), 0.00);
        assertEquals(3, order.getQuantity().doubleValue(), 0.00);
    }

    @Test
    public void test_no_such_order_found() {
        mapStore.registerOrder(TestUtils.testOrder1.getId(), TestUtils.testOrder1);
        mapStore.registerOrder(TestUtils.testOrder2.getId(), TestUtils.testOrder2);
        mapStore.registerOrder(TestUtils.testOrder3.getId(), TestUtils.testOrder3);

        Order order = mapStore.get(TestUtils.testOrder1.getId(), x -> x.getType() == OrderType.SELL);
        assertNull(order);
    }

    @Test
    public void test_register_and_cancel() {
        mapStore.registerOrder(TestUtils.testOrder1.getId(), TestUtils.testOrder1);
        mapStore.registerOrder(TestUtils.testOrder2.getId(), TestUtils.testOrder2);
        mapStore.registerOrder(TestUtils.testOrder3.getId(), TestUtils.testOrder3);

        Order order = mapStore.cancel(TestUtils.testOrder1.getId(), x -> x.getType() == OrderType.BUY);
        assertNotNull(order);

        assertEquals(311, order.getPricePerKg().doubleValue(), 0.00);
        assertEquals(3, order.getQuantity().doubleValue(), 0.00);
    }

    @Test
    public void test_no_cancellation_due_to_no_matching_order() {
        mapStore.registerOrder(TestUtils.testOrder1.getId(), TestUtils.testOrder1);
        mapStore.registerOrder(TestUtils.testOrder2.getId(), TestUtils.testOrder2);
        mapStore.registerOrder(TestUtils.testOrder3.getId(), TestUtils.testOrder3);

        Order order = mapStore.cancel(TestUtils.testOrder1.getId(), x -> x.getType() == OrderType.SELL);
        assertNull(order);
    }
}
