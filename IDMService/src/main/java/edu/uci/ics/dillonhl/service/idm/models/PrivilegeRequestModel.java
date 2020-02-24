package edu.uci.ics.dillonhl.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.dillonhl.service.idm.base.RequestModel;

public class PrivilegeRequestModel extends RequestModel {
    //@JsonProperty(value = "email", required = true)
    //private String email;

    @JsonProperty(value = "plevel", required = true)
    private Integer plevel;

    @JsonCreator
    public PrivilegeRequestModel(@JsonProperty(value = "email", required = true) String email,
                                 @JsonProperty(value = "plevel", required = true) int plevel)
    {
        //this.email = email;
        this.setEmail(email);
        this.plevel = plevel;
    }

    //@JsonProperty(value = "email", required = true)
    //public String getEmail() {return email;}

    @JsonProperty(value = "plevel", required = true)
    public int getPLevel() {return plevel;}

    public void setPlevel(Integer plevel) {
        this.plevel = plevel;
    }
}
