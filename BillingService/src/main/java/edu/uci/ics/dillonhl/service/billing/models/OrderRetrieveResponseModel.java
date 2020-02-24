package edu.uci.ics.dillonhl.service.billing.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.dillonhl.service.billing.base.ResponseModel;
import edu.uci.ics.dillonhl.service.billing.base.transactionModel;

public class OrderRetrieveResponseModel extends ResponseModel {
    @JsonProperty("transaction")
    transactionModel transactions;
    public transactionModel getTransactions() {
        return transactions;
    }

    public void setTransactions(transactionModel transactions) {
        this.transactions = transactions;
    }
}
