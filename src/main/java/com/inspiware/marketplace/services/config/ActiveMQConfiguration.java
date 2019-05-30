package com.inspiware.marketplace.services.config;

import com.inspiware.marketplace.services.utils.AMQBrokerSingletonManager;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.*;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.jms.dsl.Jms;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.destination.DynamicDestinationResolver;
import org.springframework.messaging.MessageChannel;

@EnableJms
@Configuration
public class ActiveMQConfiguration {

    private ApplicationProperties properties;

    public ActiveMQConfiguration(ApplicationProperties properties) throws Exception {
        this.properties = properties;

        AMQBrokerSingletonManager singletonManager = new AMQBrokerSingletonManager();
        singletonManager.start(true, "broker:(tcp://localhost:61616)?persistent=false&useJmx=true");
    }

    @Bean
    public DynamicDestinationResolver dynamicDestinationResolver() {
        return new DynamicDestinationResolver();
    }

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setTrustAllPackages(true);
        return connectionFactory;
    }

    @Bean
    public DefaultMessageListenerContainer listenerContainer() {
        final DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
        defaultMessageListenerContainer.setDestinationResolver(dynamicDestinationResolver());
        defaultMessageListenerContainer.setConnectionFactory(connectionFactory());
        defaultMessageListenerContainer.setDestinationName(properties.getOrderQueue());
        return defaultMessageListenerContainer;
    }

    @Bean
    public MessageChannel newOrderChannel() {
        return MessageChannels.direct("newOrderChannel").get();
    }

    @Bean
    public IntegrationFlow orderReceived() {
        return IntegrationFlows
                .from(Jms.messageDrivenChannelAdapter(listenerContainer()))
                .channel(newOrderChannel())
                .get();
    }
}
