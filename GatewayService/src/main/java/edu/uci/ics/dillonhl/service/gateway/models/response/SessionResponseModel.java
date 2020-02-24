package edu.uci.ics.dillonhl.service.gateway.models.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.dillonhl.service.gateway.models.base.ResponseModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionResponseModel extends ResponseModel {

    @JsonProperty(value = "session_id")
    private String session_id;

    public SessionResponseModel() {}

    @JsonProperty("session_id")
    public String getSessionID()
    {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }
}
