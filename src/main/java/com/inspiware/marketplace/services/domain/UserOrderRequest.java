package com.inspiware.marketplace.services.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserOrderRequest implements Serializable {

    @ApiModelProperty(value = "Order Type", allowableValues = "BUY, SELL")
    private final OrderType type; // Permissible values: BUY, SELL

    private final Quantity quantity; // Quantity of trade

    private final Price pricePerKg;

    private final String userId;

    public UserOrderRequest(@JsonProperty("userId") String userId,
                            @JsonProperty("quantity") Quantity quantity,
                            @JsonProperty("pricePerKg") Price pricePerKg,
                            @JsonProperty("type") OrderType type) {
        this.userId = userId;
        this.quantity = quantity;
        this.pricePerKg = pricePerKg;
        this.type = type;
    }

    public String getUserId() {
        return userId;
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
        return "OrderRequest{" + ", userId=" + userId +
                ", type=" + type +
                ", quantity=" + quantity +
                ", pricePerKg=" + pricePerKg +
                '}';
    }

    public static Order convertToOrder(UserOrderRequest orderRequest) {
        return new Order(orderRequest.quantity, orderRequest.pricePerKg, orderRequest.type, orderRequest.userId);
    }
}
