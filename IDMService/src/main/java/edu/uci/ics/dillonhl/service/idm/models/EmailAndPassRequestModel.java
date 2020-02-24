package edu.uci.ics.dillonhl.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.dillonhl.service.idm.base.RequestModel;


public class EmailAndPassRequestModel extends RequestModel {
    //@JsonProperty(value = "email", required = true)
    //private String email;

    @JsonProperty(value = "password", required = true)
    private char[] password;

    @JsonCreator public EmailAndPassRequestModel(@JsonProperty(value = "email", required = true) String email,
                                                 @JsonProperty(value = "password", required = true) char[] password)
    {
        //this.email = email;
        this.setEmail(email);
        this.password = password;
    }

   // @JsonProperty("email")
    //public String getEmail()
    //{
    //    return email;
    //}

    @JsonProperty("password")
    public char[] getPassword()
    {
        return password;
    }
}
