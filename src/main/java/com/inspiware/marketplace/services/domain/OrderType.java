package com.inspiware.marketplace.services.domain;

public enum OrderType {
    BUY("Buy", "DESC"), SELL("Sell", "ASC");

    private String displayName;

    private String sortingOrder;

    OrderType(String displayName, String sortingOrder)
    {
        this.displayName = displayName;
        this.sortingOrder = sortingOrder;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public String getSortingOrder() {
        return sortingOrder;
    }
}
