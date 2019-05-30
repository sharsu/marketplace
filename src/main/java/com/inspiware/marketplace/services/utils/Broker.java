package com.inspiware.marketplace.services.utils;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Broker {
    private static Logger logger = LoggerFactory.getLogger(Broker.class);

    private static BrokerService broker;

    private static boolean[] shutdown;

    private static Thread shutdownThread;

    public static void start(boolean fork, String configUri) throws Exception {

        if (broker != null) {
            logger.warn("A local broker is already running");
            return;
        }

        try {
            broker = BrokerFactory.createBroker(configUri);
            if (fork) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            broker.start();
                            waitForShutdown();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else {
                broker.start();
                waitForShutdown();
            }
        } catch (Exception e) {
            throw new Exception("Failed to start the ActiveMQ Broker", e);
        }
    }

    public static void stop() throws Exception {

        if (broker == null) {
            throw new Exception("The local broker is not running");
        }

        try {
            broker.stop();
            broker.waitUntilStopped();
            broker = null;

            Runtime.getRuntime().removeShutdownHook(shutdownThread);

            // Terminate the shutdown hook thread
            synchronized (shutdown) {
                shutdown[0] = true;
                shutdown.notify();
            }
        } catch (Exception e) {
            throw new Exception("Failed to stop the ActiveMQ Broker", e);
        }
    }

    /**
     * Wait for a shutdown invocation elsewhere
     *
     * @throws Exception
     */
    protected static void waitForShutdown() throws Exception {
        shutdown = new boolean[] { false };

        shutdownThread = new Thread() {
            @Override
            public void run() {
                synchronized (shutdown) {
                    shutdown[0] = true;
                    shutdown.notify();
                }
            }
        };

        Runtime.getRuntime().addShutdownHook(shutdownThread);

        // Wait for any shutdown event
        synchronized (shutdown) {
            while (!shutdown[0]) {
                try {
                    shutdown.wait();
                } catch (InterruptedException e) {
                }
            }
        }

        // Stop broker
        if (broker != null) {
            broker.stop();
        }
    }

    /**
     * Return the broker service created.
     */
    public static BrokerService getBroker() {
        return broker;
    }

    /**
     * Override the default creation of the broker service.  Primarily added for testing purposes.
     *
     * @param broker
     */
    public static void setBroker(BrokerService broker) {
        Broker.broker = broker;
    }
}
