package com.inspiware.marketplace.services.utils;

import org.apache.activemq.broker.BrokerService;

public class AMQBrokerSingletonManager {

    public void start(boolean fork, String configUri) throws Exception {
        Broker.start(fork, configUri);
    }

    public void stop() throws Exception {
        Broker.stop();
    }

    /**
     * Wait for a shutdown invocation elsewhere
     *
     * @throws Exception
     */
    protected void waitForShutdown() throws Exception {
        Broker.waitForShutdown();
    }

    /**
     * Return the broker service created.
     */
    public BrokerService getBroker() {
        return Broker.getBroker();
    }

    /**
     * Override the default creation of the broker service.  Primarily added for testing purposes.
     *
     * @param broker
     */
    public void setBroker(BrokerService broker) {
        Broker.setBroker(broker);
    }
}
