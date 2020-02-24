package edu.uci.ics.dillonhl.service.billing.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class transactionModel {
    @JsonProperty("capture_id")
    JsonNode capture_id;
    @JsonProperty("state")
    String state;
    @JsonProperty("amount")
    JsonNode amount;
    @JsonProperty("transaction_fee")
    JsonNode transaction_fee;
    @JsonProperty("create_time")
    JsonNode create_time;
    @JsonProperty("update_time")
    JsonNode update_time;
    @JsonProperty("items")
    itemModel[] items;

    public JsonNode getCapture_id() {
        return capture_id;
    }

    public void setCapture_id(JsonNode capture_id) {
        this.capture_id = capture_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public JsonNode getAmount() {
        return amount;
    }

    public void setAmount(JsonNode amount) {
        this.amount = amount;
    }

    public JsonNode getTransaction_fee() {
        return transaction_fee;
    }

    public void setTransaction_fee(JsonNode transaction_fee) {
        this.transaction_fee = transaction_fee;
    }

    public JsonNode getCreate_time() {
        return create_time;
    }

    public void setCreate_time(JsonNode create_time) {
        this.create_time = create_time;
    }

    public JsonNode getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(JsonNode update_time) {
        this.update_time = update_time;
    }

    public itemModel[] getItems() {
        return items;
    }

    public void setItems(itemModel[] items) {
        this.items = items;
    }
}
