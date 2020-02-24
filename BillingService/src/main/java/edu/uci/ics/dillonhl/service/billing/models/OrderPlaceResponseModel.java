package edu.uci.ics.dillonhl.service.billing.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.dillonhl.service.billing.base.ResponseModel;

public class OrderPlaceResponseModel extends ResponseModel {
    @JsonProperty (value = "approve_url")
    private String approve_url;
    @JsonProperty (value = "token")
    private String token;


    @JsonProperty ("approve_url")
    public String getApprove_url() {
        return approve_url;
    }

    @JsonProperty ("token")
    public String getToken() {
        return token;
    }

    public void setApprove_url(String approve_url) {
        this.approve_url = approve_url;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
