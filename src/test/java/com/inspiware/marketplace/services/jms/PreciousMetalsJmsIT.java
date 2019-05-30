package com.inspiware.marketplace.services.jms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inspiware.marketplace.services.MarketPlacePreciousMetalApp;
import com.inspiware.marketplace.services.domain.OrderSummary;
import com.inspiware.marketplace.services.domain.OrderType;
import com.inspiware.marketplace.services.domain.UserOrderRequest;
import com.inspiware.marketplace.services.service.PreciousMetalsService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.inspiware.marketplace.services.utils.TestUtils.getAnOrderRequest;
import static org.junit.Assert.assertEquals;

@ActiveProfiles({"test"})
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MarketPlacePreciousMetalApp.class)
public class PreciousMetalsJmsIT {

    @Autowired
    private PreciousMetalsService service;

    @Autowired
    private MessageChannel newOrderChannel;

    @Test
    public void test_publish() throws JsonProcessingException {
        UserOrderRequest orderRequest = getAnOrderRequest();
        String payload = new ObjectMapper().writeValueAsString(orderRequest);
        System.out.println("Test message: " + payload);
        QueueChannel replyChannel = new QueueChannel();
        Message<String> message = MessageBuilder.withPayload(payload).setReplyChannel(replyChannel).build();

        this.newOrderChannel.send(message);

        OrderSummary[] orderSummaries = service.orderSummary(OrderType.BUY);
        assertEquals(1, orderSummaries.length);
        assertEquals(3.3, orderSummaries[0].getQuantity().doubleValue(), 0.00);
        assertEquals(314, orderSummaries[0].getPricePerKg().doubleValue(), 0.00);
    }
}
