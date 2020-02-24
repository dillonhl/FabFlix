package edu.uci.ics.dillonhl.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.dillonhl.service.billing.base.RequestModel;

public class EMRequestModel extends RequestModel {
    @JsonProperty(value = "movie_id", required = true)
    private String movie_id;


    public EMRequestModel() {
    }

    @JsonCreator
    public EMRequestModel(@JsonProperty(value = "movie_id", required = true) String movie_id) {
        this.movie_id = movie_id;
    }

    @JsonProperty(value = "movie_id", required = true)
    public String getMovie_id() {
        return movie_id;
    }


    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

}
