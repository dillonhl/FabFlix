package edu.uci.ics.dillonhl.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.dillonhl.service.idm.base.ResponseModel;

public class PrivilegeResponseModel extends ResponseModel {

    @JsonProperty(value = "resultCode", required = true)
    private int resultCode;

    @JsonProperty(value = "message", required = true)
    private String message;


    @JsonCreator
    public PrivilegeResponseModel(int resultCode, String message)
    {
        this.resultCode = resultCode;
        this.message = message;
    }

    public PrivilegeResponseModel() {
        this.resultCode = 0;
        this.message = "Default Message";
    }

    @JsonProperty("resultCode")
    public int getResultCode() {return resultCode;}

    @JsonProperty("message")
    public String getMessage() {return message;}

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
