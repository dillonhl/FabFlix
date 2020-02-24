package edu.uci.ics.dillonhl.service.gateway.threadpool;


import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.dillonhl.service.gateway.GatewayService;
import edu.uci.ics.dillonhl.service.gateway.connectionpool.ConnectionPoolManager;
import edu.uci.ics.dillonhl.service.gateway.logger.ServiceLogger;
import edu.uci.ics.dillonhl.service.gateway.models.base.ResponseModel;
import edu.uci.ics.dillonhl.service.gateway.models.base.Result;
import edu.uci.ics.dillonhl.service.gateway.models.response.ReportResponseModel;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Worker extends Thread {
    int id;
    ThreadPool threadPool;

    private Worker(int id, ThreadPool threadPool) {
        this.id = id;
        this.threadPool = threadPool;
    }

    public static Worker CreateWorker(int id, ThreadPool threadPool)
    {
        return new Worker(id, threadPool);
    }

    public void process(ClientRequest cr) {
        System.err.println("Process starting...");
        Connection con = GatewayService.getConnectionPoolManager().requestCon();

        // construct path
        String servicePath = cr.getURI();
        String endpointPath = cr.getEndpoint();
        System.err.println("ServicePath: " + servicePath + endpointPath);
        // contact service
        // Create client
        System.err.println("RequestModel: " + cr.getRequestModel());
        ServiceLogger.LOGGER.info("Building client...");
        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);

        //Create a web target to send a request at
        ServiceLogger.LOGGER.info("Building WebTarget...");
        WebTarget webTarget = client.target(servicePath).path(endpointPath);
        System.err.println("Path: " + servicePath + endpointPath);

        // Insert query params if any
        MultivaluedMap<String,String> map = cr.getMap();
        if (map != null)
        {
            for (String key:map.keySet()) {
                System.err.println("Inserting query key: " + key + " Value: " + map.getFirst(key));
                webTarget = webTarget.queryParam(key, map.getFirst(key));
            }
        }

        // Create invocation builder
        ServiceLogger.LOGGER.info("Starting invocation builder...");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        invocationBuilder.header("email", cr.getEmail());
        System.err.println("CR.getEmail: " + cr.getEmail());
        invocationBuilder.header("session_id", cr.getSession_id());
        invocationBuilder.header("transaction_id", cr.getTransaction_id());

        // Send the request and save it to a response
        ServiceLogger.LOGGER.info("Sending request...");
        Response response;
        if (cr.isGetRequest() == true) {
            System.err.println("Get request...");
            response = invocationBuilder.get();
        }
        else if (cr.getRequestModel() != null) {
             response = invocationBuilder.post(Entity.entity(cr.getRequestModel(), MediaType.APPLICATION_JSON));
        }
        else{
            System.err.println("thumbnail request");
            response = invocationBuilder.post(Entity.entity(cr.getThumbnailRequestModel(), MediaType.APPLICATION_JSON));
        }

        ServiceLogger.LOGGER.info("Request sent.");

        ServiceLogger.LOGGER.info("Received status " + response.getStatus());

        // grab connection from connection pool, resultset is gone after connection is lost,
        // do what you need with resultset before returning connection to pool

        //ReportResponseModel responseModel = new ReportResponseModel();

        //requestModel = mapper.readValue(jsonText, RequestModel.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonText = response.readEntity(String.class);
        System.err.println("jsonText in response: " + jsonText);

        //responseModel.setMessage(jsonText);
        //responseModel.setTransaction_id(cr.getTransaction_id());

        String query = "INSERT INTO responses (transaction_id, email, session_id, response, http_status) " +
                "VALUES (?,?,?,?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setObject(1, cr.getTransaction_id());
            ps.setObject(2, cr.getEmail());
            ps.setObject(3, cr.getSession_id());
            ps.setObject(4, jsonText);
            ps.setObject(5, response.getStatus());
            ServiceLogger.LOGGER.info("Trying query " + ps);
            ps.execute();
            ServiceLogger.LOGGER.info("Query succeeded");
        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        GatewayService.getConnectionPoolManager().releaseCon(con);

    }

    @Override
    public void run() {
        while (true) {
            // take from the queue (blocking queue) CR
            // if able to take from queue, process(CR)
            ClientRequest cr = threadPool.takeRequest();
            process(cr);


        }
    }
}
