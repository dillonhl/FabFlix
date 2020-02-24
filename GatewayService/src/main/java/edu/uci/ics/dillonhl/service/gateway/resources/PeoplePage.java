package edu.uci.ics.dillonhl.service.gateway.resources;

import edu.uci.ics.dillonhl.service.gateway.GatewayService;
import edu.uci.ics.dillonhl.service.gateway.models.base.Result;
import edu.uci.ics.dillonhl.service.gateway.models.request.GeneralRequestModel;
import edu.uci.ics.dillonhl.service.gateway.models.response.GeneralResponseModel;
import edu.uci.ics.dillonhl.service.gateway.threadpool.ClientRequest;
import edu.uci.ics.dillonhl.service.gateway.transaction.TransactionGenerator;
import org.glassfish.jersey.server.Uri;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Map;

@Path("movies")
public class PeoplePage {
    @Path("people")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response moviesPeople(@Context HttpHeaders headers, @Context UriInfo uriInfo, @QueryParam("name") String name,
                                 @QueryParam("limit") Integer limit, @QueryParam("offset") Integer offset,
                                 @QueryParam("orderyby") String orderby, @QueryParam("direction") String direction)
    {
        GeneralRequestModel requestModel = new GeneralRequestModel();

        ClientRequest clientRequest = createClient(headers);
        clientRequest.setEndpoint(GatewayService.getMoviesConfigs().getPeoplePath());
        System.err.println("FUll PATH : " + clientRequest.getURI() + clientRequest.getEndpoint());
        clientRequest.setRequestModel(requestModel);
        clientRequest.setGetRequest(true);

        // Create a map for queries...
        MultivaluedMap<String, String> queries = uriInfo.getQueryParameters();
        clientRequest.setMap(queries);

        // Put request into queue
        GatewayService.getThreadPool().putRequest(clientRequest);
        System.err.println("Returning 204...");
        GeneralResponseModel responseModel = new GeneralResponseModel();
        responseModel.setResult(Result.NO_CONTENT);
        return responseModel.buildResponse(requestModel.getEmail(), clientRequest.getTransaction_id(), clientRequest.getSession_id());
        //return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Path("people/search")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response peopleSearch(@Context HttpHeaders headers, @Context UriInfo uriInfo, @QueryParam("name") String name,
                              @QueryParam("birthday") String birthday, @QueryParam("movie_title") String title,
                              @QueryParam("limit") Integer limit, @QueryParam("offset") Integer offset,
                              @QueryParam("orderby") String orderby, @QueryParam("direction") String direction) {
        GeneralRequestModel requestModel = new GeneralRequestModel();

        ClientRequest clientRequest = createClient(headers);
        clientRequest.setEndpoint(GatewayService.getMoviesConfigs().getPeopleSearchPath());
        System.err.println("FUll PATH : " + clientRequest.getURI() + clientRequest.getEndpoint());
        clientRequest.setRequestModel(requestModel);
        clientRequest.setGetRequest(true);

        // Create a map for queries...
        MultivaluedMap<String, String> queries = uriInfo.getQueryParameters();
        clientRequest.setMap(queries);

        for (String key:queries.keySet()){
            System.err.println("Key: " + key + " Value: " + queries.getFirst(key));
        }
        // Put request into queue
        GatewayService.getThreadPool().putRequest(clientRequest);
        System.err.println("Returning 204...");
        GeneralResponseModel responseModel = new GeneralResponseModel();
        responseModel.setResult(Result.NO_CONTENT);
        return responseModel.buildResponse(requestModel.getEmail(), clientRequest.getTransaction_id(), clientRequest.getSession_id());
        //return Response.status(Response.Status.NO_CONTENT).build();

    }

    @Path("people/get/{person_id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieID(@Context HttpHeaders headers, @PathParam("person_id") String person_id)
    {
        GeneralRequestModel requestModel = new GeneralRequestModel();

        ClientRequest clientRequest = createClient(headers);
        clientRequest.setEndpoint(GatewayService.getMoviesConfigs().getPeopleGetPath() + person_id);
        System.err.println("FUll PATH : " + clientRequest.getURI() + clientRequest.getEndpoint());
        clientRequest.setRequestModel(requestModel);
        clientRequest.setGetRequest(true);

        // Put request into queue
        GatewayService.getThreadPool().putRequest(clientRequest);
        System.err.println("Returning 204...");
        GeneralResponseModel responseModel = new GeneralResponseModel();
        responseModel.setResult(Result.NO_CONTENT);
        return responseModel.buildResponse(requestModel.getEmail(), clientRequest.getTransaction_id(), clientRequest.getSession_id());
        //return Response.status(Response.Status.NO_CONTENT).build();
    }

    public ClientRequest createClient(HttpHeaders headers)
    {
        ClientRequest clientRequest = new ClientRequest();
        String transaction_id = TransactionGenerator.generate();
        clientRequest.setEmail(headers.getHeaderString("email"));
        clientRequest.setSession_id(headers.getHeaderString("session_id"));
        clientRequest.setTransaction_id(transaction_id);
        String path = GatewayService.getMoviesConfigs().getScheme() + GatewayService.getMoviesConfigs().getHostName() + ":" +
                GatewayService.getMoviesConfigs().getPort() + GatewayService.getMoviesConfigs().getPath();
        clientRequest.setURI(path);

        return clientRequest;
    }
}
