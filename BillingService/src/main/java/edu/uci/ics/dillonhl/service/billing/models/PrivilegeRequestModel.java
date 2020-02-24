package edu.uci.ics.dillonhl.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PrivilegeRequestModel {
    @JsonProperty(value = "email", required = true)
    private String email;
    @JsonProperty(value = "plevel", required = true)
    private Integer plevel;

    public PrivilegeRequestModel(){}

    @JsonCreator
    public PrivilegeRequestModel(@JsonProperty(value = "email", required = true) String email,
                                 @JsonProperty(value = "privilege") Integer plevel)

    {
        this.email = email;
        this.plevel = plevel;
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

    public void setPlevel(Integer plevel) {
        this.plevel = plevel;
    }
}
