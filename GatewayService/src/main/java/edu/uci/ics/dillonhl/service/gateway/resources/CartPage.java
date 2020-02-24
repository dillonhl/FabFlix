package edu.uci.ics.dillonhl.service.gateway.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.dillonhl.service.gateway.GatewayService;
import edu.uci.ics.dillonhl.service.gateway.models.base.Result;
import edu.uci.ics.dillonhl.service.gateway.models.request.GeneralRequestModel;
import edu.uci.ics.dillonhl.service.gateway.models.request.billing.EMQRequestModel;
import edu.uci.ics.dillonhl.service.gateway.models.request.billing.EMRequestModel;
import edu.uci.ics.dillonhl.service.gateway.models.request.idm.EmailAndPassRequestModel;
import edu.uci.ics.dillonhl.service.gateway.models.response.GeneralResponseModel;
import edu.uci.ics.dillonhl.service.gateway.threadpool.ClientRequest;
import edu.uci.ics.dillonhl.service.gateway.transaction.TransactionGenerator;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("billing/cart")
public class CartPage {
    @Path("insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cartInsert(@Context HttpHeaders headers, String jsonText)
    {
        System.err.println("Start cart/insert...");
        EMQRequestModel requestModel;
        ObjectMapper mapper = new ObjectMapper();

        try {
            requestModel = mapper.readValue(jsonText, EMQRequestModel.class);
        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            else if (e instanceof JsonParseException) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        }

        ClientRequest clientRequest = createRequest(GatewayService.getBillingConfigs().getCartInsertPath(), headers);

        clientRequest.setRequestModel(requestModel);
        System.err.println("Printing requestModel items...");
        System.err.println("Email: " + requestModel.getEmail());
        System.err.println("ClientRequestModel Email: " + clientRequest.getRequestModel().getEmail());
        GatewayService.getThreadPool().putRequest(clientRequest);
        System.err.println("Returning 204...");

        GeneralResponseModel responseModel = new GeneralResponseModel();
        responseModel.setResult(Result.NO_CONTENT);
        return responseModel.buildResponse(requestModel.getEmail(), clientRequest.getTransaction_id(), clientRequest.getSession_id());
       // return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Path("update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cartUpdate(@Context HttpHeaders headers, String jsonText)
    {
        System.err.println("Start cart/update...");
        EMQRequestModel requestModel;
        ObjectMapper mapper = new ObjectMapper();

        try {
            requestModel = mapper.readValue(jsonText, EMQRequestModel.class);
        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            else if (e instanceof JsonParseException) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        }

        ClientRequest clientRequest = createRequest(GatewayService.getBillingConfigs().getCartUpdatePath(), headers);

        clientRequest.setRequestModel(requestModel);
        System.err.println("Printing requestModel items...");
        System.err.println("Email: " + requestModel.getEmail());
        System.err.println("ClientRequestModel Email: " + clientRequest.getRequestModel().getEmail());
        GatewayService.getThreadPool().putRequest(clientRequest);
        System.err.println("Returning 204...");
        GeneralResponseModel responseModel = new GeneralResponseModel();
        responseModel.setResult(Result.NO_CONTENT);
        return responseModel.buildResponse(requestModel.getEmail(), clientRequest.getTransaction_id(), clientRequest.getSession_id());
        // return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Path("delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cartDelete(@Context HttpHeaders headers, String jsonText)
    {
        System.err.println("Start cart/delete...");
        EMRequestModel requestModel;
        ObjectMapper mapper = new ObjectMapper();

        try {
            requestModel = mapper.readValue(jsonText, EMRequestModel.class);
        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            else if (e instanceof JsonParseException) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        }

        ClientRequest clientRequest = createRequest(GatewayService.getBillingConfigs().getCartDeletePath(), headers);

        clientRequest.setRequestModel(requestModel);
        System.err.println("Printing requestModel items...");
        System.err.println("Email: " + requestModel.getEmail());
        System.err.println("ClientRequestModel Email: " + clientRequest.getRequestModel().getEmail());
        GatewayService.getThreadPool().putRequest(clientRequest);
        System.err.println("Returning 204...");
        GeneralResponseModel responseModel = new GeneralResponseModel();
        responseModel.setResult(Result.NO_CONTENT);
        return responseModel.buildResponse(requestModel.getEmail(), clientRequest.getTransaction_id(), clientRequest.getSession_id());
        //return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Path("retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cartRetrieve(@Context HttpHeaders headers, String jsonText)
    {
        System.err.println("Start cart/retrieve...");
        GeneralRequestModel requestModel;
        ObjectMapper mapper = new ObjectMapper();

        try {
            requestModel = mapper.readValue(jsonText, GeneralRequestModel.class);
        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            else if (e instanceof JsonParseException) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        }

        ClientRequest clientRequest = createRequest(GatewayService.getBillingConfigs().getCartRetrievePath(), headers);

        clientRequest.setRequestModel(requestModel);
        System.err.println("Printing requestModel items...");
        System.err.println("Email: " + requestModel.getEmail());
        System.err.println("ClientRequestModel Email: " + clientRequest.getRequestModel().getEmail());
        GatewayService.getThreadPool().putRequest(clientRequest);
        System.err.println("Returning 204...");
        GeneralResponseModel responseModel = new GeneralResponseModel();
        responseModel.setResult(Result.NO_CONTENT);
        return responseModel.buildResponse(requestModel.getEmail(), clientRequest.getTransaction_id(), clientRequest.getSession_id());
        //return Response.status(Response.Status.NO_CONTENT).build();
    }


    @Path("clear")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cartClear(@Context HttpHeaders headers, String jsonText)
    {
        System.err.println("Start cart/clear...");
        GeneralRequestModel requestModel;
        ObjectMapper mapper = new ObjectMapper();

        try {
            requestModel = mapper.readValue(jsonText, GeneralRequestModel.class);
        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            else if (e instanceof JsonParseException) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        }

        ClientRequest clientRequest = createRequest(GatewayService.getBillingConfigs().getCartClearPath(), headers);

        clientRequest.setRequestModel(requestModel);
        System.err.println("Printing requestModel items...");
        System.err.println("Email: " + requestModel.getEmail());
        System.err.println("ClientRequestModel Email: " + clientRequest.getRequestModel().getEmail());
        GatewayService.getThreadPool().putRequest(clientRequest);
        System.err.println("Returning 204...");
        GeneralResponseModel responseModel = new GeneralResponseModel();
        responseModel.setResult(Result.NO_CONTENT);
        return responseModel.buildResponse(requestModel.getEmail(), clientRequest.getTransaction_id(), clientRequest.getSession_id());
        //return Response.status(Response.Status.NO_CONTENT).build();
    }

    public ClientRequest createRequest(String endpoint, HttpHeaders headers)
    {
        ClientRequest clientRequest = new ClientRequest();
        String path = GatewayService.getBillingConfigs().getScheme() + GatewayService.getBillingConfigs().getHostName() + ":" +
                GatewayService.getBillingConfigs().getPort() + GatewayService.getBillingConfigs().getPath();
        clientRequest.setURI(path);
        clientRequest.setEndpoint(endpoint);
        clientRequest.setEmail(headers.getHeaderString("email"));
        clientRequest.setSession_id(headers.getHeaderString("session_id"));
        String transaction_id = TransactionGenerator.generate();
        clientRequest.setTransaction_id(transaction_id);
        System.err.println("Printing client request variables...");
        System.err.println("Path : " + clientRequest.getURI() + clientRequest.getEndpoint());
        System.err.println("");
        return clientRequest;
    }

}
