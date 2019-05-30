package com.inspiware.marketplace.services.controller;

import com.inspiware.marketplace.services.MarketPlacePreciousMetalApp;
import com.inspiware.marketplace.services.service.PreciousMetalsService;
import com.inspiware.marketplace.services.utils.TestUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.skyscreamer.jsonassert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.Assert.assertEquals;

import static com.inspiware.marketplace.services.utils.TestUtils.getFileAsString;

@ActiveProfiles({"rest", "test"})
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MarketPlacePreciousMetalApp.class)
public class PreciousMetalsControllerIT {

    private MockMvc restMockMvc;

    @Autowired
    private PreciousMetalsService service;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        PreciousMetalsController controller = new PreciousMetalsController(service);
        this.restMockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(jacksonMessageConverter)
                .build();
    }

    @Before
    public void initTests() {
        TestUtils.getAllOrders().forEach(p -> service.registerOrder(p));
    }

    @Test
    public void test_orders_by_user_id_1() throws Exception {
        MvcResult mvcResult = restMockMvc.perform(get("/api/marketplace/precious/metals/silver-bars/order-book")
                .header("USER_ID", "TEST_USER_1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andReturn();

        JSONArray actualJsonArray = (JSONArray) JSONParser.parseJSON(mvcResult.getResponse().getContentAsString());

        assertEquals(6, actualJsonArray.length());
    }

    @Test
    public void test_orders_by_user_id_2() throws Exception {
        MvcResult mvcResult = restMockMvc.perform(get("/api/marketplace/precious/metals/silver-bars/order-book")
                .header("USER_ID", "TEST_USER_2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andReturn();
        JSONArray actualJsonArray = (JSONArray) JSONParser.parseJSON(mvcResult.getResponse().getContentAsString());

        assertEquals(4, actualJsonArray.length());
    }

    @Test
    public void test_orders_by_user_id_3() throws Exception {
        String expectedResult = getFileAsString("/data/order-book-response1.json");

        MvcResult mvcResult = restMockMvc.perform(get("/api/marketplace/precious/metals/silver-bars/order-book")
                .header("USER_ID", "TEST_USER_3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andReturn();

        JSONArray actualJsonArray = (JSONArray) JSONParser.parseJSON(mvcResult.getResponse().getContentAsString());

        assertEquals(5, actualJsonArray.length());
    }

    @Test
    public void test_order_book_summary_for_buy_orders() throws Exception {
        String expectedResult = getFileAsString("/data/order-book-response1.json");

        MvcResult mvcResult = restMockMvc.perform(get("/api/marketplace/precious/metals/silver-bars/order-book/summary/BUY"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andReturn();

        JSONAssert.assertEquals(expectedResult, mvcResult.getResponse().getContentAsString(), JSONCompareMode.STRICT);
    }

    @Test
    public void test_order_book_summary_for_sell_orders() throws Exception {
        String expectedResult = getFileAsString("/data/order-book-response2.json");

        MvcResult mvcResult = restMockMvc.perform(get("/api/marketplace/precious/metals/silver-bars/order-book/summary/SELL"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andReturn();

        JSONAssert.assertEquals(expectedResult, mvcResult.getResponse().getContentAsString(), JSONCompareMode.STRICT);
    }

    @Test
    public void test_register_a_buy_order_and_then_cancel() throws Exception {
        // Register an order
        MvcResult mvcResult = restMockMvc.perform(post("/api/marketplace/precious/metals/silver-bars/register")
                .header("USER_ID", "TEST_USER_3")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"type\":\"BUY\",\"quantity\":{\"value\":6.50,\"unit\":\"kg\"},\"pricePerKg\":{\"value\":310,\"currency\":\"GBP\"}}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andReturn();

        JSONObject object = (JSONObject) JSONParser.parseJSON(mvcResult.getResponse().getContentAsString());
        String orderId = (String) object.get("orderId");

        mvcResult = restMockMvc.perform(get("/api/marketplace/precious/metals/silver-bars/order-book/summary/BUY"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andReturn();

        String expectedResult = getFileAsString("/data/order-book-response3.json");
        JSONAssert.assertEquals(expectedResult, mvcResult.getResponse().getContentAsString(), JSONCompareMode.STRICT);

        // Cancel the order
        restMockMvc.perform(get("/api/marketplace/precious/metals/silver-bars/cancel/{orderId}", orderId)
                .header("USER_ID", "TEST_USER_3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(jsonPath("$.transactionType").value(1))
                .andExpect(jsonPath("$.orderId").value(orderId))
                .andExpect(jsonPath("$.orderStatus").value(3));

        mvcResult = restMockMvc.perform(get("/api/marketplace/precious/metals/silver-bars/order-book/summary/BUY"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andReturn();

        expectedResult = getFileAsString("/data/order-book-response1.json");
        JSONAssert.assertEquals(expectedResult, mvcResult.getResponse().getContentAsString(), JSONCompareMode.STRICT);
    }

    @Test
    public void test_register_a_sell_order_and_then_cancel() throws Exception {
        // Register an order
        MvcResult mvcResult = restMockMvc.perform(post("/api/marketplace/precious/metals/silver-bars/register")
                .header("USER_ID", "TEST_USER_1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"type\":\"SELL\",\"quantity\":{\"value\":2.10,\"unit\":\"kg\"},\"pricePerKg\":{\"value\":310,\"currency\":\"GBP\"}}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andReturn();

        JSONObject object = (JSONObject) JSONParser.parseJSON(mvcResult.getResponse().getContentAsString());
        String orderId = (String) object.get("orderId");

        mvcResult = restMockMvc.perform(get("/api/marketplace/precious/metals/silver-bars/order-book/summary/SELL"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andReturn();

        String expectedResult = getFileAsString("/data/order-book-response4.json");
        JSONAssert.assertEquals(expectedResult, mvcResult.getResponse().getContentAsString(), JSONCompareMode.STRICT);

        // Cancel the order
        restMockMvc.perform(get("/api/marketplace/precious/metals/silver-bars/cancel/{orderId}", orderId)
                .header("USER_ID", "TEST_USER_1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(jsonPath("$.transactionType").value(1))
                .andExpect(jsonPath("$.orderId").value(orderId))
                .andExpect(jsonPath("$.orderStatus").value(3));

        mvcResult = restMockMvc.perform(get("/api/marketplace/precious/metals/silver-bars/order-book/summary/SELL"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andReturn();

        expectedResult = getFileAsString("/data/order-book-response2.json");
        JSONAssert.assertEquals(expectedResult, mvcResult.getResponse().getContentAsString(), JSONCompareMode.STRICT);
    }

    @Test
    public void test_cancel_an_non_existing_order() throws Exception {
        String orderId = UUID.randomUUID().toString();

        restMockMvc.perform(get("/api/marketplace/precious/metals/silver-bars/cancel/{orderId}", orderId)
                .header("USER_ID", "TEST_USER_3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(jsonPath("$.transactionType").value(1))
                .andExpect(jsonPath("$.orderId").value(orderId))
                .andExpect(jsonPath("$.orderStatus").value(2))
                .andExpect(jsonPath("$.rejectReason").value(2))
                .andExpect(jsonPath("$.rejectionMessage").value("Unknown order"));

        MvcResult mvcResult = restMockMvc.perform(get("/api/marketplace/precious/metals/silver-bars/order-book")
                .header("USER_ID", "TEST_USER_3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andReturn();

        JSONArray actualJsonArray = (JSONArray) JSONParser.parseJSON(mvcResult.getResponse().getContentAsString());

        assertEquals(5, actualJsonArray.length());
    }

}
