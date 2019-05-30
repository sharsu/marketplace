package com.inspiware.marketplace.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.config.EnableIntegration;

@EnableIntegration
@SpringBootApplication
public class MarketPlacePreciousMetalApp
{
    private static final Logger log = LoggerFactory.getLogger(MarketPlacePreciousMetalApp.class);

    public static void main(String[] args) {
        SpringApplication.run(MarketPlacePreciousMetalApp.class, args);
    }
}
