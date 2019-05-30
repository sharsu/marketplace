package com.inspiware.marketplace.services.config;

import com.inspiware.marketplace.services.domain.UserOrderRequest;
import com.inspiware.marketplace.services.service.PreciousMetalsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;

@Service
public class ActiveMQEndPoint {

    @Autowired
    private PreciousMetalsService preciousMetalsService;

    @ServiceActivator(inputChannel = "newOrderChannel")
    public void processMessage(final UserOrderRequest orderRequest) {
        // Register order
        preciousMetalsService.registerOrder(UserOrderRequest.convertToOrder(orderRequest));
    }
}
