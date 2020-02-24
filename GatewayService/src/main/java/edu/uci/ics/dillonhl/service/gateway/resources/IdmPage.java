package edu.uci.ics.dillonhl.service.gateway.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.dillonhl.service.gateway.GatewayService;
import edu.uci.ics.dillonhl.service.gateway.models.base.RequestModel;
import edu.uci.ics.dillonhl.service.gateway.models.base.Result;
import edu.uci.ics.dillonhl.service.gateway.models.request.GeneralRequestModel;
import edu.uci.ics.dillonhl.service.gateway.models.request.idm.EmailAndPassRequestModel;
import edu.uci.ics.dillonhl.service.gateway.models.request.idm.PrivilegeRequestModel;
import edu.uci.ics.dillonhl.service.gateway.models.request.idm.SessionRequestModel;
import edu.uci.ics.dillonhl.service.gateway.models.response.GeneralResponseModel;
import edu.uci.ics.dillonhl.service.gateway.threadpool.ClientRequest;
import edu.uci.ics.dillonhl.service.gateway.transaction.TransactionGenerator;


import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;

@Path("idm")
public class IdmPage {
    @Path("register")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(@Context HttpHeaders headers, String jsonText)
    {
        System.err.println("Start idm/register...");
        String transaction_id = TransactionGenerator.generate();
        //IdmHelper.sendPostRequest(headers, jsonText, transaction_id);
        EmailAndPassRequestModel requestModel;
        ObjectMapper mapper = new ObjectMapper();

        try {
            requestModel = mapper.readValue(jsonText, EmailAndPassRequestModel.class);
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
        ClientRequest clientRequest = new ClientRequest();
        String path = GatewayService.getIdmConfigs().getScheme() + GatewayService.getIdmConfigs().getHostName() + ":" +
                GatewayService.getIdmConfigs().getPort() + GatewayService.getIdmConfigs().getPath();
        clientRequest.setURI(path);
        clientRequest.setEndpoint(GatewayService.getIdmConfigs().getRegisterPath());
        clientRequest.setEmail(requestModel.getEmail());
        clientRequest.setTransaction_id(transaction_id);
        clientRequest.setRequestModel(requestModel);
        GatewayService.getThreadPool().putRequest(clientRequest);

        System.err.println("Transaction_id: " + clientRequest.getTransaction_id());
        System.err.println("Returning 204...");

        GeneralResponseModel responseModel = new GeneralResponseModel();
        responseModel.setResult(Result.NO_CONTENT);
        return responseModel.buildResponse(requestModel.getEmail(), clientRequest.getTransaction_id(), null);

        /*Response.ResponseBuilder rb = Response.noContent();
        return rb.header("email", requestModel.getEmail())
            .header("transaction_id", transaction_id).build();*/
    }

    @Path("login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Context HttpHeaders headers, String jsonText) {
        System.err.println("Start idm/login...");
        EmailAndPassRequestModel requestModel;
        ObjectMapper mapper = new ObjectMapper();

        try {
            requestModel = mapper.readValue(jsonText, EmailAndPassRequestModel.class);
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

        ClientRequest clientRequest = createRequest(GatewayService.getIdmConfigs().getLoginPath(), requestModel.getEmail());

        clientRequest.setRequestModel(requestModel);
        System.err.println("Printing requestModel items...");
        System.err.println("Email: " + requestModel.getEmail());
        System.err.println("ClientRequestModel Email: " + clientRequest.getRequestModel().getEmail());
        GatewayService.getThreadPool().putRequest(clientRequest);
        System.err.println("Returning 204...");
        GeneralResponseModel responseModel = new GeneralResponseModel();
        responseModel.setResult(Result.NO_CONTENT);
        return responseModel.buildResponse(requestModel.getEmail(), clientRequest.getTransaction_id(), null);
        //return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Path("session")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response session(@Context HttpHeaders headers, String jsonText) {
        System.err.println("Start idm/session...");
        SessionRequestModel requestModel;
        ObjectMapper mapper = new ObjectMapper();

        try {
            requestModel = mapper.readValue(jsonText, SessionRequestModel.class);
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

        ClientRequest clientRequest = createRequest(GatewayService.getIdmConfigs().getSessionPath(), requestModel.getEmail());

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

    @Path("privilege")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response privilege(@Context HttpHeaders headers, String jsonText) {
        System.err.println("Start idm/privilege...");
        PrivilegeRequestModel requestModel;
        ObjectMapper mapper = new ObjectMapper();

        try {
            requestModel = mapper.readValue(jsonText, PrivilegeRequestModel.class);
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
        // headers has no email
        ClientRequest clientRequest = createRequest(GatewayService.getIdmConfigs().getPrivilegePath(), requestModel.getEmail());

        clientRequest.setRequestModel(requestModel);
        clientRequest.setResponseModel(new GeneralResponseModel());
        System.err.println("Printing requestModel items...");
        System.err.println("Email: " + requestModel.getEmail());
        System.err.println("ClientRequestModel Email: " + clientRequest.getRequestModel().getEmail());
        GatewayService.getThreadPool().putRequest(clientRequest);
        System.err.println("Returning 204...");

        GeneralResponseModel responseModel = new GeneralResponseModel();
        responseModel.setResult(Result.NO_CONTENT);
        return responseModel.buildResponse(requestModel.getEmail(), clientRequest.getTransaction_id(), null);
        //return Response.status(Response.Status.NO_CONTENT).build();
    }

    public ClientRequest createRequest(String endpointPath, String email)
    {
        ClientRequest clientRequest = new ClientRequest();
        String path = GatewayService.getIdmConfigs().getScheme() + GatewayService.getIdmConfigs().getHostName() + ":" +
                GatewayService.getIdmConfigs().getPort() + GatewayService.getIdmConfigs().getPath();
        clientRequest.setURI(path);
        clientRequest.setEndpoint(endpointPath);
        clientRequest.setEmail(email);
        //clientRequest.setEmail(headers.getHeaderString("email"));
        //clientRequest.setSession_id(headers.getHeaderString("session_id"));
        String transaction_id = TransactionGenerator.generate();
        clientRequest.setTransaction_id(transaction_id);
        System.err.println("Printing client request variables...");
        System.err.println("Path : " + clientRequest.getURI() + clientRequest.getEndpoint());
        System.err.println("");
        return clientRequest;
    }

}