package edu.uci.ics.dillonhl.service.gateway.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.dillonhl.service.gateway.GatewayService;
import edu.uci.ics.dillonhl.service.gateway.logger.ServiceLogger;
import edu.uci.ics.dillonhl.service.gateway.models.base.Result;
import edu.uci.ics.dillonhl.service.gateway.models.request.idm.SessionRequestModel;
import edu.uci.ics.dillonhl.service.gateway.models.response.ReportResponseModel;
import edu.uci.ics.dillonhl.service.gateway.models.response.SessionResponseModel;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Path("report")
public class ReportPage {
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response report(@Context HttpHeaders headers, String jsonString)
    {
        System.err.println("START REPORT...\n      Headers: \nEmail: " + headers.getHeaderString("email"));
        Connection con = GatewayService.getConnectionPoolManager().requestCon();

        String email = headers.getHeaderString("email");
        String transaction_id = headers.getHeaderString("transaction_id");
        String session_id = headers.getHeaderString("session_id");

        System.err.println("Session_id: " + session_id);

        /*//
        SessionResponseModel sessionResponse = new SessionResponseModel();
        // check session by contacting idm
        // Create client
        SessionRequestModel requestModel = new SessionRequestModel(email, session_id);
        String servicePath = GatewayService.getIdmConfigs().getScheme() + GatewayService.getIdmConfigs().getHostName() + ":" +
                GatewayService.getIdmConfigs().getPort() + GatewayService.getIdmConfigs().getPath();
        String endpointPath = GatewayService.getIdmConfigs().getSessionPath();
        ServiceLogger.LOGGER.info("Building client...");
        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);

        //Create a web target to send a request at
        ServiceLogger.LOGGER.info("Building WebTarget...");
        WebTarget webTarget = client.target(servicePath).path(endpointPath);

        // Create invocation builder
        ServiceLogger.LOGGER.info("Starting invocation builder...");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        // Send the request and save it to a response
        ServiceLogger.LOGGER.info("Sending request...");
        Response response = invocationBuilder.post(Entity.entity(requestModel, MediaType.APPLICATION_JSON));
        ServiceLogger.LOGGER.info("Request sent.");

        ServiceLogger.LOGGER.info("Received status " + response.getStatus());

        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonText = response.readEntity(String.class);
            System.err.println("jsonText in response: " + jsonText);
            sessionResponse = mapper.readValue(jsonText, SessionResponseModel.class);
            ServiceLogger.LOGGER.info("Successfully mapped response.");
            System.err.println("Response model trying to get resultCode: " + sessionResponse.getResultCode() +
                    "\nMessage: " + sessionResponse.getMessage());
        } catch (IOException e)
        {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }


        int sessionStatus = sessionResponse.getResultCode();
        if (sessionStatus != 130)
        {
            ServiceLogger.LOGGER.info("Session not active.");
            return sessionResponse.buildResponse();
        }*/

        Response report = null;

        ResultSet rs = checkTransactionID(transaction_id, email, con);
        int status = 204;
        try {
            if (rs.next())
            {
                // request delay?
                System.err.println("FOUND SOMETHING IN RESPONSES......");
                Response.ResponseBuilder rb = Response.ok();
                report = rb.header("message", rs.getString("response"))
                        .header("request_delay", 1)
                        .header("transaction_id", transaction_id).build();
                status = rs.getInt("http_status");

                deleteResponse(transaction_id, con);
            }
            else
            {
                System.err.println("FOUND NOTHING IN RESPONSES....");
                Response.ResponseBuilder rb = Response.noContent();
                report = rb.header("message", "Waiting on response...")
                        .header("transaction_id",transaction_id).build();

                //deleteResponse(transaction_id, con);
            }
        }catch(SQLException e)
        {
        }

        GatewayService.getConnectionPoolManager().releaseCon(con);
        return report;
    }

    public ResultSet checkTransactionID(String transaction_id, String email, Connection con)
    {
        try {
            String query = "SELECT * FROM responses" +
                    " WHERE transaction_id = ? && email = ?;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setObject(1, transaction_id);
            ps.setObject(2, email);
            ServiceLogger.LOGGER.info("Trying query " + ps);
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            if (rs.next())
            {
                System.err.println(rs.getString("transaction_id"));
            }
            rs.beforeFirst();

            return rs;

        } catch(SQLException e)
        {
            System.err.println("Error checking transaction_id");
            e.printStackTrace();
            return null;
        }
    }

    public void deleteResponse(String transaction_id, Connection con)
    {
        try {
            String query = "DELETE FROM responses WHERE transaction_id = ?;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setObject(1, transaction_id);
            ServiceLogger.LOGGER.info("Trying query " + ps);
            ps.execute();
            ServiceLogger.LOGGER.info("Query succeeded.");

        }catch(SQLException e)
        {
            ServiceLogger.LOGGER.info("Query failed...");
        }
    }
}
