package edu.uci.ics.dillonhl.service.movies.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.dillonhl.service.movies.MoviesService;
import edu.uci.ics.dillonhl.service.movies.base.GenResponseModel;
import edu.uci.ics.dillonhl.service.movies.base.RequestModel;
import edu.uci.ics.dillonhl.service.movies.base.ResponseModel;
import edu.uci.ics.dillonhl.service.movies.base.Result;
import edu.uci.ics.dillonhl.service.movies.configs.IdmConfigs;
import edu.uci.ics.dillonhl.service.movies.core.Movie;
import edu.uci.ics.dillonhl.service.movies.logger.ServiceLogger;
import edu.uci.ics.dillonhl.service.movies.models.MovieSearchResponseModel;
import edu.uci.ics.dillonhl.service.movies.models.PrivilegeRequestModel;
import edu.uci.ics.dillonhl.service.movies.models.data.MovieModel;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.*;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
@Path("/")
public class PhrasePage {
    @Path("browse/{phrase}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response movieBrowse(@PathParam("phrase") String phrase, @Context HttpHeaders headers,
                                @QueryParam("limit") Integer limit, @QueryParam("offset") Integer offset,
                                @QueryParam("orderby") String orderby, @QueryParam("direction") String direction) {
        System.err.println("Browse Phrase: " + phrase);
        IdmConfigs idm = MoviesService.getIdmConfigs();
        String servicePath = idm.getScheme() + idm.getHostName() + ":" + idm.getPort() + idm.getPath();
        String endpointPath = idm.getPrivilegePath();
        System.err.println("ServicePath: " + servicePath);
        System.err.println("EndpointPath: " + endpointPath);

        //header strings
        String email = headers.getHeaderString("email");
        String session_id = headers.getHeaderString("session_id");
        String transaction_id = headers.getHeaderString("transaction_id");

        System.err.println("Email: " + email + "\nSession_id: " + session_id + "\nTransaction_id: " + transaction_id);

        PrivilegeRequestModel requestModel = new PrivilegeRequestModel(4);
        requestModel.setEmail(email);
        GenResponseModel genResponseModel = new GenResponseModel();
        MovieSearchResponseModel responseModel = new MovieSearchResponseModel();

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

        // Send the request and save it to a response
        ServiceLogger.LOGGER.info("Sending request...");
        Response response = invocationBuilder.post(Entity.entity(requestModel, MediaType.APPLICATION_JSON));
        ServiceLogger.LOGGER.info("Request sent.");

        ServiceLogger.LOGGER.info("Received status " + response.getStatus());

        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonText = response.readEntity(String.class);
            System.err.println("jsonText in response: " + jsonText);
            genResponseModel = mapper.readValue(jsonText, GenResponseModel.class);
            ServiceLogger.LOGGER.info("Successfully mapped response.");
            System.err.println("Response model trying to get resultCode: " + genResponseModel.getResultCode() +
                    "\nMessage: " + genResponseModel.getMessage());
        } catch (IOException e)
        {
            e.printStackTrace();
            responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        }
        int pResult = genResponseModel.getResultCode();

        String[] keywords = phrase.split(",");
        for (String keyword: keywords)
        {
            System.err.println("Key: " + keyword);
        }

        ServiceLogger.LOGGER.info("Received request to search movies keywords");

        String query = buildPhraseQuery(keywords, limit, offset, orderby, direction);

        MovieModel[] movies = Movie.browseMovies(query, pResult);

        if (movies == null || movies.length == 0)
        {
            System.err.println("No movies found...");
            responseModel.setResult(Result.NO_MOVIE_FOUND);
        }
        else
        {
            responseModel.setMovies(movies);
            responseModel.setResult(Result.FOUND_MOVIES);
        }

        return responseModel.buildResponse();
    }

    public String buildPhraseQuery(String[] keywords, Integer limit, Integer offset, String orderby, String direction)
    {

        if (limit == null || (limit != 10 && limit != 25 && limit != 50))
            limit = 10;
        if (offset == null || offset < 0 || offset % 5 != 0)
            offset = 0;
        if (orderby == null || orderby.isEmpty() || (!orderby.equals("title") && !orderby.equals("rating") && !orderby.equals("year")))
            orderby = "title";
        if (direction == null || direction.isEmpty())
            direction = "asc";

        String selectFrom = "SELECT * FROM movie m JOIN (SELECT kim.movie_id, GROUP_CONCAT(DISTINCT k.name ORDER BY kim.keyword_id) as keywords_in_movie\n" +
                "FROM keyword_in_movie kim JOIN keyword k on kim.keyword_id = k.keyword_id\n" +
                "   GROUP BY movie_id) as keywords on m.movie_id = keywords.movie_id";
        String WHERE = " WHERE 1=1";
        for (String keyword:keywords)
        {
            WHERE += " && keywords_in_movie LIKE '%" + keyword + "%' ";
        }
        WHERE += " ORDER BY " + orderby + " " + direction + " LIMIT " + limit + " OFFSET " + offset + ";";

        return selectFrom + WHERE;
    }
}