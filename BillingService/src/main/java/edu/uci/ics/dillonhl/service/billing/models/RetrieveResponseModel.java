package edu.uci.ics.dillonhl.service.billing.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.dillonhl.service.billing.base.ResponseModel;
import edu.uci.ics.dillonhl.service.billing.base.itemModel;

public class RetrieveResponseModel extends ResponseModel {
    @JsonProperty("items")
    private itemModel[] items;

    public RetrieveResponseModel() {}

    public itemModel[] getItems() {
        return items;
    }

    public void setItems(itemModel[] items) {
        this.items = items;
    }
}
