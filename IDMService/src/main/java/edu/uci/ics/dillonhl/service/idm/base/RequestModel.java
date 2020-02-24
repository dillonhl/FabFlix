package edu.uci.ics.dillonhl.service.idm.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class RequestModel  {
    @JsonProperty(value = "email", required = true)
    private String email;

    public RequestModel() {}

    @JsonCreator
    public RequestModel(@JsonProperty(value = "email", required = true) String email)
    {
        this.email = email;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
