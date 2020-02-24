package edu.uci.ics.dillonhl.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailRequestModel {
    @JsonProperty(value = "email", required = true)
    private String email;

    public EmailRequestModel(){}

    @JsonCreator
    public EmailRequestModel(@JsonProperty(value = "email", required = true) String email)
    {
        this.email = email;
    }

    @JsonProperty(value = "email", required = true)
    public String getEmail()
    {
        return email;
    }


    public void setEmail(String email)
    {
        this.email = email;
    }
}
