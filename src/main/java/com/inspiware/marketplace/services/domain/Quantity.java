package com.inspiware.marketplace.services.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Objects;

public class Quantity implements Serializable {
    private static final long serialVersionUID = 6504081836032983882L;

    private final BigDecimal value;

    private final Unit unit;

    public Quantity(@JsonProperty("value") BigDecimal value, @JsonProperty("unit") Unit unit) {
        this.value = value;
        this.unit = unit;
    }

    public BigDecimal getValue() {
        return value;
    }

    public Unit getUnit() {
        return unit;
    }

    public double doubleValue() {
        return value.doubleValue();
    }

    public Quantity to(Unit unit) {
        if (this.unit.equals(unit)) {
            return this;
        }
        return new Quantity(value.multiply(unit.getConvesion()).divide(this.unit.getConvesion()), unit);
    }

    public Quantity add(Quantity that) {
        if (getUnit().equals(that.getUnit())) {
            return new Quantity(value.add(toBigDecimal(that.getValue()), MathContext.DECIMAL128), getUnit());
        }
        Quantity converted = that.to(getUnit());
        return new Quantity(value.add(toBigDecimal(converted.getValue())), getUnit());
    }

    public Quantity cancel() {
        return new Quantity(value.negate(), getUnit());
    }

    /*
     * (non-Javadoc)
     *
     * @see AbstractQuantity#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (obj instanceof Quantity) {
            Quantity that = (Quantity) obj;
            return Objects.equals(getUnit(), that.getUnit()) && hasEquality(value, that.getValue());
        }
        return false;
    }

    @Override
    public String toString() {
        return "Quantity{" +
                "value=" + value +
                ", unit=" + unit +
                '}';
    }

    public static BigDecimal toBigDecimal(Number value) {
        if (BigDecimal.class.isInstance(value)) {
            return BigDecimal.class.cast(value);
        } else if (BigInteger.class.isInstance(value)) {
            return new BigDecimal(BigInteger.class.cast(value));
        }
        return BigDecimal.valueOf(value.doubleValue());
    }

    public static boolean hasEquality(Number valueA, Number valueB) {
        Objects.requireNonNull(valueA);
        Objects.requireNonNull(valueB);
        if (valueA instanceof Double && valueB instanceof Double) {
            return valueA.doubleValue() == valueB.doubleValue();
        } else if (valueA instanceof Float && valueB instanceof Float) {
            return valueA.floatValue() == valueB.floatValue();
        } else if (valueA instanceof Integer && valueB instanceof Integer) {
            return valueA.intValue() == valueB.intValue();
        } else if (valueA instanceof Long && valueB instanceof Long) {
            return valueA.longValue() == valueB.longValue();
        } else if (valueA instanceof Short && valueB instanceof Short) {
            return valueA.shortValue() == valueB.shortValue();
        } else if (valueA instanceof Byte && valueB instanceof Byte) {
            return valueA.byteValue() == valueB.byteValue();
        }
        return toBigDecimal(valueA).compareTo(toBigDecimal(valueB)) == 0;
    }
}
