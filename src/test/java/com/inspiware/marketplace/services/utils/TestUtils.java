package com.inspiware.marketplace.services.utils;

import com.inspiware.marketplace.services.controller.PreciousMetalsControllerIT;
import com.inspiware.marketplace.services.domain.*;
import org.assertj.core.util.Files;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

public class TestUtils {

    private static final Price p1 = new Price(new BigDecimal(306), null);
    private static final Price p2 = new Price(new BigDecimal(307), null);
    private static final Price p3 = new Price(new BigDecimal(310), null);
    private static final Price p4 = new Price(new BigDecimal(311), null);
    private static final Price p5 = new Price(new BigDecimal(314), null);

    private static final Quantity q1 = new Quantity(new BigDecimal(3.0).setScale(2, BigDecimal.ROUND_HALF_UP), Unit.KG);
    private static final Quantity q2 = new Quantity(new BigDecimal(3.1).setScale(2, BigDecimal.ROUND_HALF_UP), Unit.KG);
    private static final Quantity q3 = new Quantity(new BigDecimal(3.2).setScale(2, BigDecimal.ROUND_HALF_UP), Unit.KG);
    private static final Quantity q4 = new Quantity(new BigDecimal(3.3).setScale(2, BigDecimal.ROUND_HALF_UP), Unit.KG);
    private static final Quantity q5 = new Quantity(new BigDecimal(3.4).setScale(2, BigDecimal.ROUND_HALF_UP), Unit.KG);
    private static final Quantity q6 = new Quantity(new BigDecimal(3.5).setScale(2, BigDecimal.ROUND_HALF_UP), Unit.KG);
    private static final Quantity q7 = new Quantity(new BigDecimal(3.6).setScale(2, BigDecimal.ROUND_HALF_UP), Unit.KG);
    private static final Quantity q8 = new Quantity(new BigDecimal(3.7).setScale(2, BigDecimal.ROUND_HALF_UP), Unit.KG);

    // USER 1 orders - BUY and SELL mixed
    public static final Order testOrder1 = new Order(q1, p4, OrderType.BUY, "TEST_USER_1");
    public static final Order testOrder2 = new Order(q3, p1, OrderType.BUY, "TEST_USER_1");
    public static final Order testOrder3 = new Order(q4, p5, OrderType.BUY, "TEST_USER_1");
    public static final Order testOrder4 = new Order(q5, p3, OrderType.BUY, "TEST_USER_1");
    public static final Order testOrder5 = new Order(q2, p2, OrderType.SELL, "TEST_USER_1");
    public static final Order testOrder6 = new Order(q8, p1, OrderType.SELL, "TEST_USER_1");

    // USER 2 orders - BUY and SELL mixed
    public static final Order testOrder7 = new Order(q7, p2, OrderType.BUY, "TEST_USER_2");
    public static final Order testOrder8 = new Order(q2, p4, OrderType.SELL, "TEST_USER_2");
    public static final Order testOrder9 = new Order(q5, p1, OrderType.SELL, "TEST_USER_2");
    public static final Order testOrder10 = new Order(q6, p5, OrderType.BUY, "TEST_USER_2");

    // USER 3 orders - BUY and SELL mixed
    public static final Order testOrder11 = new Order(q2, p4, OrderType.BUY, "TEST_USER_3");
    public static final Order testOrder12 = new Order(q5, p1, OrderType.BUY, "TEST_USER_3");
    public static final Order testOrder13 = new Order(q3, p2, OrderType.SELL, "TEST_USER_3");
    public static final Order testOrder14 = new Order(q7, p5, OrderType.BUY, "TEST_USER_3");
    public static final Order testOrder15 = new Order(q6, p3, OrderType.SELL, "TEST_USER_3");

    public static List<Order> getAllOrders() {
        return Arrays.asList(testOrder1, testOrder2, testOrder3, testOrder4, testOrder5, testOrder6,
                testOrder7, testOrder8, testOrder9, testOrder10, testOrder11, testOrder12, testOrder13, testOrder14,
                testOrder15);
    }

    public static Order getAnotherOrder() {
        return new Order(q4, p5, OrderType.BUY, "TEST_USER_1");
    }

    public static UserOrderRequest getAnOrderRequest() {
        return new UserOrderRequest("TEST_USER_4", q4, p5, OrderType.BUY);
    }

    public static String getFileAsString(final String fileName) {
        return Files.contentOf(new File(PreciousMetalsControllerIT.class.getResource(fileName).getFile()), Charset.defaultCharset());
    }
}
