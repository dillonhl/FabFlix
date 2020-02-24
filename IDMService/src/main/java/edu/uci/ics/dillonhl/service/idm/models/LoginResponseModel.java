package edu.uci.ics.dillonhl.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.dillonhl.service.idm.base.ResponseModel;

public class LoginResponseModel extends ResponseModel {
    @JsonProperty(value = "resultCode", required = true)
    private int resultCode;

    @JsonProperty(value = "message", required = true)
    private String message;

    @JsonProperty(value = "session_id")
    private String session_id;

    // @JsonProperty (value = "salt")
    //private String salt;

    //@JsonProperty(value = "hashedPassword")
    // private String hashedPassword;

    @JsonCreator
    public LoginResponseModel(int resultCode, String message, String session_id)
    {
        this.resultCode = resultCode;
        this.message = message;
        this.session_id = session_id;
    }

    public LoginResponseModel() {

    }

    @JsonProperty("resultCode")
    public int getResultCode()
    {
        return resultCode;
    }

    @JsonProperty("message")
    public String getMessage()
    {
        return message;
    }

    @JsonProperty("session_id")
    public String getSession_id()
    {
        return session_id;
    }
}
