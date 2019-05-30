package com.inspiware.marketplace.services.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Price implements Comparable<Price>, Serializable {

    private final BigDecimal value;

    private final Currency currency;

    public Price(@JsonProperty("value") BigDecimal value, @JsonProperty("currency") Currency currency) {
        this.value = value;
        if (currency != null) {
            this.currency = currency;
        } else {
            this.currency = Currency.getInstance(Locale.UK);
        }
    }

    public BigDecimal getValue() {
        return value;
    }

    public Currency getCurrency() {
        return currency;
    }

    public double doubleValue() {
        return value.doubleValue();
    }

    @Override
    public String toString() {
        return "Price{" +
                "value=" + value +
                ", currency=" + currency +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Objects.equals(value, price.value) &&
                Objects.equals(currency, price.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, currency);
    }

    @Override
    public int compareTo(Price o) {
        // Don't have the mechanism to compare currencies
        // therefore the comparison will only be based on
        // price value
        return this.value.compareTo(o.value);
    }
}
