package com.inspiware.marketplace.services.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

import java.util.Objects;
import java.util.UUID;

@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
final public class Order implements Identifiable<UUID> {

    @JsonProperty("orderId")
    private final UUID id;

    @JsonProperty("type")
    private final OrderType type; // Permissible values: BUY, SELL

    @JsonProperty("quantity")
    private final Quantity quantity; // Quantity of trade

    @JsonProperty("pricePerKg")
    private final Price pricePerKg;

    @JsonIgnore
    private String userId;

    public Order(Quantity quantity,
                 Price pricePerKg,
                 OrderType type,
                 String userId) {
        this.id = UUID.randomUUID();
        this.quantity = quantity;
        this.pricePerKg = pricePerKg;
        this.type = type;
        this.userId = userId;
    }

    public UUID getId() {
        return id;
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

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + id +
                ", type=" + type +
                ", quantity=" + quantity +
                ", pricePerKg=" + pricePerKg +
                ", userId='" + userId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Order order = (Order) o;
        return id == order.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, quantity, pricePerKg, userId);
    }

    public static Order fromOrderRequest(OrderRequest orderRequest, String userId) {
        return new Order(orderRequest.getQuantity(), orderRequest.getPricePerKg(), orderRequest.getType(), userId);
    }

    public Order cancelOrder() {
        return new Order(quantity.cancel(), pricePerKg, type, userId);
    }
}
