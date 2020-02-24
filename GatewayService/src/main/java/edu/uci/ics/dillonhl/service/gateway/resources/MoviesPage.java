package edu.uci.ics.dillonhl.service.gateway.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.dillonhl.service.gateway.GatewayService;
import edu.uci.ics.dillonhl.service.gateway.models.base.Result;
import edu.uci.ics.dillonhl.service.gateway.models.request.GeneralRequestModel;
import edu.uci.ics.dillonhl.service.gateway.models.request.idm.SessionRequestModel;
import edu.uci.ics.dillonhl.service.gateway.models.request.movies.ThumbnailRequestModel;
import edu.uci.ics.dillonhl.service.gateway.models.response.GeneralResponseModel;
import edu.uci.ics.dillonhl.service.gateway.threadpool.ClientRequest;
import edu.uci.ics.dillonhl.service.gateway.transaction.TransactionGenerator;
import org.glassfish.jersey.server.Uri;

import javax.ws.rs.*;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.util.ArrayList;

@Path("movies")
public class MoviesPage {
    @Path("search")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response movieSearch(@Context HttpHeaders headers, @Context UriInfo uriInfo,
                                @QueryParam("title") String title, @QueryParam("year") Integer year,
                                @QueryParam("director") String director, @QueryParam("genre") String genre, @QueryParam("hidden") Boolean hidden,
                                @QueryParam("limit") Integer limit, @QueryParam("offset") Integer offset, @QueryParam("orderby") String orderby,
                                @QueryParam("direction") String direction)
    {
        GeneralRequestModel requestModel = new GeneralRequestModel();

        ClientRequest clientRequest = createClient(headers);
        String endpointPath = GatewayService.getMoviesConfigs().getSearchPath();// + getQueryEndpointPath(uriInfo);
        clientRequest.setEndpoint(endpointPath);
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
        return responseModel.buildResponse(clientRequest.getEmail(), clientRequest.getTransaction_id(), clientRequest.getSession_id());
        //return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Path("browse/{phrase}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response movieBrowse(@Context HttpHeaders headers, @Context UriInfo uriInfo, @PathParam("phrase") String phrase,
                                @QueryParam("title") String title, @QueryParam("year") Integer year,
                                @QueryParam("director") String director, @QueryParam("genre") String genre, @QueryParam("hidden") Boolean hidden,
                                @QueryParam("limit") Integer limit, @QueryParam("offset") Integer offset, @QueryParam("orderby") String orderby,
                                @QueryParam("direction") String direction)
    {
        GeneralRequestModel requestModel = new GeneralRequestModel();

        ClientRequest clientRequest = createClient(headers);
        String endpointPath = GatewayService.getMoviesConfigs().getBrowsePath() + phrase;// + getQueryEndpointPath(uriInfo);
        clientRequest.setEndpoint(endpointPath);
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
        return responseModel.buildResponse(clientRequest.getEmail(), clientRequest.getTransaction_id(), clientRequest.getSession_id());
        //return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Path("thumbnail")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response thumbnail(@Context HttpHeaders headers, String jsonText)
    {
        System.err.println("Start Movies/thumbnail...");
        ThumbnailRequestModel requestModel = new ThumbnailRequestModel();
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode nodes = mapper.readTree(jsonText).get("movie_ids");
            ArrayList<String> movies = new ArrayList<>();
            for (JsonNode node : nodes) {
                movies.add(node.asText());
            }
            System.err.println("Size of list: " + movies.size());
            String[] movie_ids = new String[movies.size()];
            for (int i = 0; i < movies.size(); i++)
            {
                movie_ids[i] = movies.get(i);
                System.err.println("Movie ID at index " + i + ": " + movie_ids[i]);
            }
            requestModel.setMovie_ids(movie_ids);
        } catch (IOException e) {
            e.printStackTrace();

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        ClientRequest clientRequest = createClient(headers);
        clientRequest.setEndpoint(GatewayService.getMoviesConfigs().getThumbnailPath());
        System.err.println("FUll PATH : " + clientRequest.getURI() + clientRequest.getEndpoint());
        clientRequest.setThumbnailRequestModel(requestModel);
        clientRequest.setRequestModel(null);

        // Put request into queue
        GatewayService.getThreadPool().putRequest(clientRequest);
        System.err.println("Returning 204...");
        GeneralResponseModel responseModel = new GeneralResponseModel();
        responseModel.setResult(Result.NO_CONTENT);
        return responseModel.buildResponse(clientRequest.getEmail(), clientRequest.getTransaction_id(), clientRequest.getSession_id());
        //return Response.status(Response.Status.NO_CONTENT).build();

    }

    @Path("get/{movie_id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieID(@Context HttpHeaders headers, @PathParam("movie_id") String movie_id)
    {
        GeneralRequestModel requestModel = new GeneralRequestModel();

        ClientRequest clientRequest = createClient(headers);
        clientRequest.setEndpoint(GatewayService.getMoviesConfigs().getGetPath() + movie_id);
        System.err.println("FUll PATH : " + clientRequest.getURI() + clientRequest.getEndpoint());
        clientRequest.setRequestModel(requestModel);
        clientRequest.setGetRequest(true);

        // Put request into queue
        GatewayService.getThreadPool().putRequest(clientRequest);
        System.err.println("Returning 204...");
        GeneralResponseModel responseModel = new GeneralResponseModel();
        responseModel.setResult(Result.NO_CONTENT);
        return responseModel.buildResponse(clientRequest.getEmail(), clientRequest.getTransaction_id(), clientRequest.getSession_id());
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

    public String getQueryEndpointPath(UriInfo uriInfo)
    {
        int index = uriInfo.getRequestUri().toString().indexOf("?");
        String fullEndpointPath = uriInfo.getRequestUri().toString().substring(index);
        System.err.println("Endpoint query path: " + fullEndpointPath);
        return fullEndpointPath;
    }
}
