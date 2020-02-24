package edu.uci.ics.dillonhl.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.dillonhl.service.billing.base.RequestModel;

// Email, movie_id, quantity request model
public class EMQRequestModel extends RequestModel {
    @JsonProperty(value = "movie_id", required = true)
    private String movie_id;

    @JsonProperty(value = "quantity", required = true)
    private Integer quantity;

    public EMQRequestModel() {}

    @JsonCreator
    public EMQRequestModel(@JsonProperty(value = "movie_id", required = true)  String movie_id,
                           @JsonProperty(value = "quantity", required = true)  Integer quantity)
    {
        this.movie_id = movie_id;
        this.quantity = quantity;
    }

    @JsonProperty(value = "movie_id", required = true)
    public String getMovie_id() {
        return movie_id;
    }
    @JsonProperty(value = "quantity", required = true)
    public Integer getQuantity() {
        return quantity;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
