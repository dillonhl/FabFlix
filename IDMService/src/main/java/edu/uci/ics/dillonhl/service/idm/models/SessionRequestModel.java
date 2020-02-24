package edu.uci.ics.dillonhl.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.dillonhl.service.idm.base.RequestModel;
import edu.uci.ics.dillonhl.service.idm.security.Session;

public class SessionRequestModel extends RequestModel {
    //@JsonProperty(value = "email", required = true)
    //private String email;
    @JsonProperty(value = "session_id", required = true)
    private String session_id;

    public SessionRequestModel() {}

    /*@JsonCreator
    public SessionRequestModel(@JsonProperty(value = "email", required = true) String email,
                               @JsonProperty(value = "session_id") String session_id) {
        //this.email = email;
        this.setEmail(email);
        this.session_id = session_id;
    }*/

    //@JsonProperty(value = "email", required = true)
    //public String getEmail() {
        //return email;
    //}

    @JsonProperty(value = "session_id")
    public String getSession_id() {
        return session_id;
    }
}

