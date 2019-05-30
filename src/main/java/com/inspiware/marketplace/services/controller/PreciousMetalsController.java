package com.inspiware.marketplace.services.controller;

import com.inspiware.marketplace.services.domain.*;
import com.inspiware.marketplace.services.service.PreciousMetalsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Api(value="/precious/metals", tags = "precious-metals", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, basePath = "/api/marketplace")
@RestController
@RequestMapping(value = "/api/marketplace/precious/metals", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
public class PreciousMetalsController
{
    private static final Logger log = LoggerFactory.getLogger(PreciousMetalsController.class);

    private PreciousMetalsService preciousMetalsService;

    public PreciousMetalsController(PreciousMetalsService preciousMetalsService) {
        this.preciousMetalsService = preciousMetalsService;
    }

    @ApiOperation(value = "Fetch orders placed by user",
            notes = "Returns a set of orders for the logged user",
            response = Order.class,
            responseContainer = "Set"
    )
    @GetMapping("/silver-bars/order-book")
    public Set<Order> allOrdersByUser(@RequestHeader("USER_ID") String userId) {
        log.info(String.format("Fetching all orders for the logged in user %s", userId));
        Set<Order> orders;
        try {
            orders = preciousMetalsService.findAllByUserId(userId);
        } catch (Exception ex) {
            log.error(String.format("Unable to fetch prices for user %s", userId), ex);
            orders = Collections.emptySet();
        }
        return orders;
    }

    @ApiOperation(value = "Fetch order book summary",
            notes = "Returns the order book summary for the given order type",
            response = OrderSummary.class,
            responseContainer = "Array"
    )
    @GetMapping("/silver-bars/order-book/summary/{orderType}")
    public OrderSummary[] orderBookSummary(@ApiParam(value = "Type of the orders to be fetched", allowableValues = "BUY,SELL", required = true)
                                           @PathVariable("orderType") String orderType) {
        log.info(String.format("Fetching order summary for the order type %s", orderType));
        OrderSummary[] orders;
        try {
            orders = preciousMetalsService.orderSummary(OrderType.valueOf(orderType));
        } catch (Exception ex) {
            log.error(String.format("Fetching order summary for the order type %s", orderType), ex);
            orders = new OrderSummary[0];
        }
        return orders;
    }

    @ApiOperation(value = "Register a new order",
            notes = "Registers the order for logged user",
            response = Acknowledgement.class
    )
    @PostMapping("/silver-bars/register")
    public ResponseEntity<Acknowledgement> register(@RequestBody OrderRequest orderRequest,
                                                    @RequestHeader("USER_ID") String userId) {
        Acknowledgement responseAck = null;
        HttpStatus httpStatus = HttpStatus.CREATED;
        // Set user id provided in the header
        Order order = Order.fromOrderRequest(orderRequest, userId);
        log.info("Registering order " + order);
        try {
            responseAck = preciousMetalsService.registerOrder(order);
        } catch (Exception ex) {
            log.error(String.format("Failed while registering order %s", order), ex);
            responseAck = Acknowledgement.failedAck(order.getId(), ex.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Acknowledgement>(responseAck, httpStatus);
    }

    @ApiOperation(value = "Cancel an order",
            notes = "This method will cancel an existing order if exists and registered by logged in user.",
            response = Acknowledgement.class
    )
    @GetMapping("/silver-bars/cancel/{orderId}")
    public ResponseEntity<Acknowledgement> cancel(@PathVariable("orderId") UUID orderId, @RequestHeader("USER_ID") String userId) {
        Acknowledgement responseAck = null;
        HttpStatus httpStatus = HttpStatus.OK;
        log.info("Cancelling order " + orderId);
        try {
            responseAck = preciousMetalsService.cancelOrder(orderId, userId);
        } catch (Exception ex) {
            log.error(String.format("Failed while cancelling order %s", orderId), ex);
            responseAck = Acknowledgement.failedAck(orderId, ex.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Acknowledgement>(responseAck, httpStatus);
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception ex) {
        log.error("Error occurred while processing the request", ex);
        return ex.getMessage();
    }
}
