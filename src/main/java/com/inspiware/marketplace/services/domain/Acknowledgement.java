package com.inspiware.marketplace.services.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.logging.log4j.util.Strings;

import java.util.UUID;

@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
final public class Acknowledgement {

    @JsonProperty("messageId")
    private final UUID messageId;

    @ApiModelProperty(value = "Transaction Type", allowableValues = "0 - NEW, 1 - CANCEL")
    @JsonProperty("transactionType")
    private final int transactionType; // permissible values 0 - New, 1 - Cancel

    @JsonProperty("orderId")
    private final UUID orderId;

    @JsonProperty("origOrderId")
    private final UUID origOrderId;

    @ApiModelProperty(value = "Order Status", allowableValues = "0 - Received, 1 - Accepted, 2 - Rejected, 3 - Cancelled")
    @JsonProperty("orderStatus")
    private final int orderStatus; // Permissible values 0 - Received, 1 - Accepted, 2 - Rejected, 3 - Cancelled

    @ApiModelProperty(value = "Reject Reason", allowableValues = "0 - No rejection, 1 - Invalid party information, 2 - Unknown order, 3 - Unauthorized, 4 - Invalid Order, 99 - Other error")
    @JsonProperty("rejectReason")
    private final int rejectReason; // Permissible values 0 - No rejection, 1 - Invalid party information, 2 - Unknown order, 3 - Unauthorized, 4 - Invalid Order, 99 - Other error

    @JsonProperty("rejectionMessage")
    private final String rejectionMessage;

    private Acknowledgement(UUID messageId, int transactionType, UUID orderId, int orderStatus,
                            int rejectReason, String rejectionMessage) {
        this.messageId = messageId;
        this.transactionType = transactionType;
        this.orderId = orderId;
        this.origOrderId = orderId;
        this.orderStatus = orderStatus;
        this.rejectReason = rejectReason;
        this.rejectionMessage = rejectionMessage;
    }

    private Acknowledgement(UUID messageId, int transactionType, UUID orderId, int orderStatus) {
        this.messageId = messageId;
        this.transactionType = transactionType;
        this.orderId = orderId;
        this.origOrderId = orderId;
        this.orderStatus = orderStatus;
        this.rejectReason = 0;
        this.rejectionMessage = Strings.EMPTY;
    }

    private Acknowledgement(UUID messageId, int transactionType, UUID orderId, UUID origOrderId, int orderStatus) {
        this.messageId = messageId;
        this.transactionType = transactionType;
        this.orderId = orderId;
        this.origOrderId = origOrderId;
        this.orderStatus = orderStatus;
        this.rejectReason = 0;
        this.rejectionMessage = Strings.EMPTY;
    }

    public static Acknowledgement failedAck(final UUID origOrderId, final String rejectionMessage) {
        return new Acknowledgement(UUID.randomUUID(), 0, origOrderId, 2, 99, rejectionMessage);
    }

    public static Acknowledgement newAck(final UUID origOrderId) {
        return new Acknowledgement(UUID.randomUUID(), 0, origOrderId, 1);
    }

    public static Acknowledgement cancelAck(final UUID orderId, final UUID origOrderId) {
        return new Acknowledgement(UUID.randomUUID(), 1, orderId, origOrderId, 3);
    }

    public static Acknowledgement cancelNack(final UUID orderId) {
        return new Acknowledgement(UUID.randomUUID(), 1, orderId, 2, 2, "Unknown order");
    }

    public static Acknowledgement nack(final UUID orderId, final String rejectionMessage) {
        return new Acknowledgement(UUID.randomUUID(), 0, orderId, 2, 4, rejectionMessage);
    }

    public static Acknowledgement unauthorized(final UUID orderId, final String rejectionMessage) {
        return new Acknowledgement(UUID.randomUUID(), 0, orderId, 2, 3, rejectionMessage);
    }

    public static Acknowledgement unknown(final UUID orderId) {
        return new Acknowledgement(UUID.randomUUID(), 0, orderId, 2, 2, "Unknown order");
    }
}
