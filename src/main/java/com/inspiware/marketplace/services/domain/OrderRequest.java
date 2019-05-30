package com.inspiware.marketplace.services.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
final public class OrderRequest implements Serializable {

    @ApiModelProperty(value = "Order Type", allowableValues = "BUY, SELL")
    private final OrderType type; // Permissible values: BUY, SELL

    private final Quantity quantity; // Quantity of trade

    private final Price pricePerKg;

    public OrderRequest(@JsonProperty("quantity") Quantity quantity,
                        @JsonProperty("pricePerKg") Price pricePerKg,
                        @JsonProperty("type") OrderType type) {
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
        return "OrderRequest{" + ", type=" + type +
                ", quantity=" + quantity +
                ", pricePerKg=" + pricePerKg +
                '}';
    }
}
