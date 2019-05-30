package com.inspiware.marketplace.services.service;

import com.inspiware.marketplace.services.domain.*;
import com.inspiware.marketplace.services.store.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;

@Service
@Transactional
public class PreciousMetalsService {
    private static final Logger log = LoggerFactory.getLogger(PreciousMetalsService.class);

    private final Store<UUID, Order> orderStore;

    public PreciousMetalsService(Store orderStore) {
        this.orderStore = orderStore;
    }

    public Set<Order> findAllByUserId(final String userId) {
        return orderStore.getAll(byUser(userId));
    }

    public OrderSummary[] orderSummary(final OrderType orderType) {
        Set<Order> orders = orderStore.getAll(isOrderType(orderType));

        Map<Price, OrderSummary> orderSummaryMap = orders.stream()
                .map(OrderSummary::fromOrder)
                .collect(groupingBy(OrderSummary::getPricePerKg,
                        collectingAndThen(
                                reducing((a, b) -> new OrderSummary(a.getQuantity().add(b.getQuantity()), a.getPricePerKg(), a.getType())), Optional::get)));

        OrderSummary[] orderSummaries = orderSummaryMap.values().toArray(new OrderSummary[orderSummaryMap.size()]);
        Arrays.sort(orderSummaries);
        return orderSummaries;
    }

    public Acknowledgement registerOrder(final Order order) {
        // Store the price
        orderStore.registerOrder(order.getId(), order);

        return Acknowledgement.newAck(order.getId());
    }

    public Acknowledgement cancelOrder(final UUID orderId, final String userId) {
        Order cancelledOrder = orderStore.cancel(orderId, byUser(userId));
        if (cancelledOrder == null) {
            return Acknowledgement.cancelNack(orderId);
        }
        return Acknowledgement.cancelAck(orderId, orderId);
    }

    private Predicate<Order> isOrderType(OrderType orderType) {
        return p -> p.getType().equals(orderType);
    }

    private Predicate<Order> byUser(String userId) {
        return p -> p.getUserId().equalsIgnoreCase(userId);
    }
}
