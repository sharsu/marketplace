package com.inspiware.marketplace.services.domain;

import com.fasterxml.jackson.annotation.JsonValue;

import java.math.BigDecimal;

public enum Unit
{
    KG("kg"), POUNDS("lb", new BigDecimal(2.204623));

    private String displayName;

    private BigDecimal convesion;

    Unit(String displayName)
    {
        this.displayName = displayName;
        this.convesion = new BigDecimal(1.0);
    }

    Unit(String displayName, BigDecimal convesion)
    {
        this.displayName = displayName;
        this.convesion = convesion;
    }

    @JsonValue
    public String getDisplayName()
    {
        return displayName;
    }

    public BigDecimal getConvesion()
    {
        return convesion;
    }
}
