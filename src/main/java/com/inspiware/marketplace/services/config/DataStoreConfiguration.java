package com.inspiware.marketplace.services.config;

import com.inspiware.marketplace.services.domain.Order;
import com.inspiware.marketplace.services.exception.UnknownDatastoreTypeException;
import com.inspiware.marketplace.services.store.PreciousMetalsMapStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
@EnableConfigurationProperties(ApplicationProperties.class)
public class DataStoreConfiguration {
    private final Logger log = LoggerFactory.getLogger(DataStoreConfiguration.class);

    private ApplicationProperties properties;

    public DataStoreConfiguration(ApplicationProperties properties) {
        this.properties = properties;
    }

    @Bean(name = "preciousMetalsMapStore")
    public PreciousMetalsMapStore<UUID, Order> preciousMetalsMapStore() {
        if (properties.getDataStoreType() == null || properties.getDataStoreType().equals("MAP")) {
            return new PreciousMetalsMapStore<>("preciousMetalsMapStore");
        } else {
            throw new UnknownDatastoreTypeException("Unknown datastore type");
        }
    }
}
