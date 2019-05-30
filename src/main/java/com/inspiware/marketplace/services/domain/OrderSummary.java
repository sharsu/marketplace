package com.inspiware.marketplace.services.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

import java.math.BigDecimal;

@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
final public class OrderSummary implements Comparable<OrderSummary> {

    @JsonProperty("type")
    private final OrderType type; // Permissible values: BUY, SELL

    @JsonProperty("quantity")
    private final Quantity quantity; // Quantity of trade

    @JsonProperty("pricePerKg")
    private final Price pricePerKg;

    public OrderSummary(Quantity quantity,
                        Price pricePerKg,
                        OrderType type) {
        this.quantity = quantity;
        this.pricePerKg = pricePerKg;
        this.type = type;
    }

    public OrderType getType() {
        return type;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Price getPricePerKg() {
        return pricePerKg;
    }

    @Override
    public String toString() {
        return "Order{" + ", type=" + type +
                ", quantity=" + quantity +
                ", pricePerKg=" + pricePerKg +
                '}';
    }

    public static OrderSummary fromOrder(Order order) {
        return new OrderSummary(order.getQuantity(), order.getPricePerKg(), order.getType());
    }

    @Override
    public int compareTo(OrderSummary o) {
        if (o.type != type)
            return 0;
        if(o.type.getSortingOrder().equalsIgnoreCase("ASC"))
            return pricePerKg.compareTo(o.pricePerKg);
        else
            return o.pricePerKg.compareTo(pricePerKg);
    }
}
