package edu.uci.ics.dillonhl.service.gateway.models.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.dillonhl.service.gateway.logger.ServiceLogger;
import edu.uci.ics.dillonhl.service.gateway.models.base.ResponseModel;
import edu.uci.ics.dillonhl.service.gateway.models.base.Result;

import javax.ws.rs.core.Response;

public class ReportResponseModel extends ResponseModel {
    public ReportResponseModel() {
    }

    public ReportResponseModel(Result result) {
        super(result);
    }

    public Response buildResponse(String transaction_id) {
        ServiceLogger.LOGGER.info("Response being built with Result: " + this.getResult());

        if (this.getResult() == null || this.getResult().getStatus() == Response.Status.INTERNAL_SERVER_ERROR)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        Response.ResponseBuilder rb;
        rb = Response.status(this.getResult().getStatus()).entity(this);
        rb.header("message", "Waiting for response...");
        rb.header("request_delay", System.currentTimeMillis());
        rb.header("transaction_id", transaction_id);
        return rb.build();
    }
}
