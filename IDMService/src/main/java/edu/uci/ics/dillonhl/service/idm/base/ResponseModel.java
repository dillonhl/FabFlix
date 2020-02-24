package edu.uci.ics.dillonhl.service.idm.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.dillonhl.service.idm.logger.ServiceLogger;

import javax.ws.rs.core.Response;

public abstract class ResponseModel {
    @JsonIgnore
    private Result result;

    public ResponseModel() {}

    public ResponseModel(Result result)
    {
        this.result = result;
    }

    @JsonProperty("resultCode")
    public int getResultCode()
    {
        return result.getResultCode();
    }

    @JsonProperty("message")
    public String getMessage()
    {
        return result.getMessage();
    }

    @JsonIgnore
    public Result getResult()
    {
        return result;
    }

    @JsonIgnore
    public void setResult(Result result)
    {
        this.result = result;
    }

    @JsonIgnore
    public Response buildResponse()
    {
        ServiceLogger.LOGGER.info("Response being built with Result: " + result);

        if (result == null || result.getStatus() == Response.Status.INTERNAL_SERVER_ERROR)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        return Response.status(result.getStatus()).entity(this).build();
    }

    @JsonIgnore
    public Response buildResponse(String email, String transaction_id, String session_id)
    {
        ServiceLogger.LOGGER.info("Response being built with Result: " + result);

        if (result == null || result.getStatus() == Response.Status.INTERNAL_SERVER_ERROR)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        Response.ResponseBuilder rb;
        rb = Response.status(result.getStatus()).entity(this);
        rb.header("email", email);
        rb.header("transaction_id", transaction_id);
        rb.header("session_id", session_id);
        return rb.build();
    }

}
