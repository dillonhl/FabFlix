package edu.uci.ics.dillonhl.service.movies.base;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GenResponseModel{
    @JsonProperty("resultCode")
    public int resultCode;
    @JsonProperty("message")
    public String message;

    public GenResponseModel() {}

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

