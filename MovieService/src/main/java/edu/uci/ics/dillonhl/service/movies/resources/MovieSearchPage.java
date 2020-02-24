package edu.uci.ics.dillonhl.service.movies.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.PreparedQuery;
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
import javax.ws.rs.core.*;
import javax.xml.ws.Service;
import java.io.IOException;
import java.util.ArrayList;

@Path("search")
public class MovieSearchPage {
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response movieSearch(@Context HttpHeaders headers, @Context UriInfo uriInfo, @QueryParam("title") String title, @QueryParam("year") Integer year,
                                @QueryParam("director") String director, @QueryParam("genre") String genre, @QueryParam("hidden") Boolean hidden,
                                @QueryParam("limit") Integer limit, @QueryParam("offset") Integer offset, @QueryParam("orderby") String orderby,
                                @QueryParam("direction") String direction)
    {
        if (genre == "" || genre.isEmpty())
        {
            genre = null;
            System.err.println("genre empty.....\n\n\n");
        }

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
        ServiceLogger.LOGGER.info("Received title from query param: " + title +
                                    "\n\t\tReceived year from query param: " + year +
                                    "\n\t\tReceived director from query param: " + director +
                                    "\n\t\tReceived genre from query param: " + genre +
                                    "\n\t\tReceived hidden from query param: " + hidden +
                                    "\n\t\tReceived limit from query param: " + limit +
                                    "\n\t\tReceived offset from query param: " + offset +
                                    "\n\t\tReceived orderby from query param: " + orderby +
                                    "\n\t\tReceived direction from query param: " + direction);
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
            return responseModel.buildResponse();
        }
        int pResult = genResponseModel.getResultCode();

        ServiceLogger.LOGGER.info("Received request to search for movies");

        if (limit == null || (limit != 10 && limit != 25 && limit != 50))
            limit = 10;
        if (offset == null || offset < 0 || offset % 5 != 0)
            offset = 0;
        if (orderby == null || orderby.isEmpty() || (orderby != "title" && orderby != "rating" && orderby != "year"))
            orderby = "title";
        if (direction == null || direction.isEmpty())
            direction = "asc";

        System.err.println("Building query...");
        String SELECT = "SELECT DISTINCT m.movie_id, m.title, m.year, m.director_id, m.rating, " +
                "m.backdrop_path, m.poster_path, m.hidden, p.name";
        String FROM = " FROM movie m LEFT JOIN person p ON m.director_id = p.person_id" +
                " LEFT JOIN (SELECT movie_id, name as genre FROM genre g JOIN genre_in_movie gim on g.genre_id = gim.genre_id) ge ON ge.movie_id = m.movie_id";
        String WHERE = " WHERE 1=1";

        if (title != null) {
            WHERE += " && title LIKE '%" + title + "%'";
        }
        if (year != null) {
            WHERE += " && year = " + year;
        }
        if (director != null) {
            WHERE += " && name LIKE '%" + director + "%'";
        }
        if (genre != null) {
            WHERE += " && ge.genre = '" + genre + "'";
        }
        if (hidden != null) {
        }
        WHERE += " ORDER BY " + orderby + " " + direction + " LIMIT " + limit + " OFFSET " + offset;

        System.err.println("Query built: " + SELECT + FROM + WHERE);

        String query = SELECT + FROM + WHERE;//Movie.buildMovieQuery(title,year,director,genre,hidden,limit,offset,orderby,direction);

        MovieModel[] movies = Movie.searchMovies(query, pResult);
        System.err.println("Finished seraching....");

        if (movies == null)
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
}
