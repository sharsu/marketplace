package com.inspiware.marketplace.services.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "marketplace.precious.metals.services", ignoreUnknownFields = false)
public class ApplicationProperties {
    private String dataStoreType;
    private String feedTopics;

    // Queues
    private String orderQueue;
    private String errorQueue;

    public String getDataStoreType() {
        return dataStoreType;
    }

    public ApplicationProperties setDataStoreType(String dataStoreType) {
        this.dataStoreType = dataStoreType;
        return this;
    }

    public String getFeedTopics() {
        return feedTopics;
    }

    public ApplicationProperties setFeedTopics(String feedTopics) {
        this.feedTopics = feedTopics;
        return this;
    }

    public String getOrderQueue() {
        return orderQueue;
    }

    public ApplicationProperties setOrderQueue(String orderQueue) {
        this.orderQueue = orderQueue;
        return this;
    }

    public String getErrorQueue() {
        return errorQueue;
    }

    public ApplicationProperties setErrorQueue(String errorQueue) {
        this.errorQueue = errorQueue;
        return this;
    }
}