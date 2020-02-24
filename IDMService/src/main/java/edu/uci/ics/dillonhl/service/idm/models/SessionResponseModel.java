package edu.uci.ics.dillonhl.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.dillonhl.service.idm.base.ResponseModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionResponseModel extends ResponseModel {

    //@JsonProperty(value = "resultCode", required = true)
    //private int resultCode;

   // @JsonProperty(value = "message", required = true)
   // private String message;

    @JsonProperty(value = "session_id")
    private String session_id;

    public SessionResponseModel() {}

    @JsonCreator
    public SessionResponseModel(int resultCode, String message, String session_id)
    {
        //this.resultCode = resultCode;
        //this.message = message;
        this.session_id = session_id;
    }

    /*@JsonProperty("resultCode")
    public int getResultCode()
    {
        return resultCode;
    }

    @JsonProperty("message")
    public String getMessage()
    {
        return message;
    }*/

    @JsonProperty("session_id")
    public String getSessionID()
    {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }
}
