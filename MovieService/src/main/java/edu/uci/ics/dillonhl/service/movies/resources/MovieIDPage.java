package edu.uci.ics.dillonhl.service.movies.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.dillonhl.service.movies.MoviesService;
import edu.uci.ics.dillonhl.service.movies.base.GenResponseModel;
import edu.uci.ics.dillonhl.service.movies.base.RequestModel;
import edu.uci.ics.dillonhl.service.movies.base.ResponseModel;
import edu.uci.ics.dillonhl.service.movies.base.Result;
import edu.uci.ics.dillonhl.service.movies.configs.ConfigsModel;
import edu.uci.ics.dillonhl.service.movies.configs.IdmConfigs;
import edu.uci.ics.dillonhl.service.movies.configs.ServiceConfigs;
import edu.uci.ics.dillonhl.service.movies.core.Movie;
import edu.uci.ics.dillonhl.service.movies.logger.ServiceLogger;
import edu.uci.ics.dillonhl.service.movies.models.MovieIDResponseModel;
import edu.uci.ics.dillonhl.service.movies.models.MovieSearchResponseModel;
import edu.uci.ics.dillonhl.service.movies.models.PrivilegeRequestModel;
import edu.uci.ics.dillonhl.service.movies.models.data.MovieModel;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.*;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import javax.xml.ws.Service;
import java.io.IOException;
import java.util.Map;

@Path("/")
public class MovieIDPage {
    @Path("get/{movie_id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response movieSearch(@Context HttpHeaders headers, @PathParam("movie_id") String movie_id, String json) {
        IdmConfigs idm = MoviesService.getIdmConfigs();
        String servicePath = idm.getScheme() + idm.getHostName() + ":" + idm.getPort() + idm.getPath();
        String endpointPath = idm.getPrivilegePath();
        System.err.println("ServicePath: " + servicePath);
        System.err.println("EndpointPath: " + endpointPath);
        //String servicePath = "http://localhost:5333/api/idm";
        //String endpointPath = "/privilege";

        //header strings
        String email = headers.getHeaderString("email");
        String session_id = headers.getHeaderString("session_id");
        String transaction_id = headers.getHeaderString("transaction_id");

        System.err.println("Email: " + email + "\nSession_id: " + session_id + "\nTransaction_id: " + transaction_id);

        PrivilegeRequestModel requestModel = new PrivilegeRequestModel(4);
        requestModel.setEmail(email);
        GenResponseModel genResponseModel = new GenResponseModel();
        MovieIDResponseModel responseModel = new MovieIDResponseModel();

        // Create client
        ServiceLogger.LOGGER.info("Building client...");
        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);

        //Create a web target to send a request at
        ServiceLogger.LOGGER.info("Building WebTarget...");
        WebTarget webTarget = client.target(servicePath).path(endpointPath);

        // Create invocation builder
        ServiceLogger.LOGGER.info("Starting invocation builder...");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        invocationBuilder.header("email", email);

        // Send the request and save it to a response
        ServiceLogger.LOGGER.info("Sending request...");
        Response response = invocationBuilder.post(Entity.entity(requestModel, MediaType.APPLICATION_JSON));
        ServiceLogger.LOGGER.info("Request sent.");

        ServiceLogger.LOGGER.info("Received status " + response.getStatus());

        try {
            //requestModel = mapper.readValue(jsonText, RequestModel.class);
            ObjectMapper mapper = new ObjectMapper();
            String jsonText = response.readEntity(String.class);
            System.err.println("jsonText in response: " + jsonText);
            // JsonNode node = mapper.readTree(jsonText);
            //Integer result = node.get("resultCode");
            genResponseModel = mapper.readValue(jsonText, GenResponseModel.class);
            ServiceLogger.LOGGER.info("Successfully mapped response.");
            System.err.println("Response model trying to get resultCode: " + genResponseModel.getResultCode() +
                    "\nMessage: " + genResponseModel.getMessage());
        } catch (IOException e)
        {
            e.printStackTrace();
            responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
            return responseModel.buildResponse();
        }
        int pResult = genResponseModel.getResultCode();

        System.err.println("User privilege result: " + pResult);


        ServiceLogger.LOGGER.info("Received request to search movie with movie_id: " + movie_id);

        MovieModel movie = Movie.getMovie(movie_id,pResult);
        if (movie == null)
        {
            responseModel.setResult(Result.NO_MOVIE_FOUND);
            return responseModel.buildResponse();
        }
        else
        {
            responseModel.setMovie(movie);
            responseModel.setResult(Result.FOUND_MOVIES);
        }


        return responseModel.buildResponse();
    }
}