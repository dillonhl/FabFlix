package edu.uci.ics.dillonhl.service.movies.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.dillonhl.service.movies.base.Result;
import edu.uci.ics.dillonhl.service.movies.core.Movie;
import edu.uci.ics.dillonhl.service.movies.models.ThumbnailRequestModel;
import edu.uci.ics.dillonhl.service.movies.models.ThumbnailResponseModel;
import edu.uci.ics.dillonhl.service.movies.models.data.ThumbnailModel;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;

@Path("thumbnail")
public class ThumbnailPage {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response movieSearch(@Context HttpHeaders headers, String jsonText) {

        //header strings
        String email = headers.getHeaderString("email");
        String session_id = headers.getHeaderString("session_id");
        String transaction_id = headers.getHeaderString("transaction_id");

        System.err.println("Email: " + email + "\nSession_id: " + session_id + "\nTransaction_id: " + transaction_id);

        ThumbnailRequestModel requestModel = new ThumbnailRequestModel();
        ThumbnailResponseModel responseModel = new ThumbnailResponseModel();
        ObjectMapper mapper = new ObjectMapper();
        System.err.println();

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
            //requestModel = mapper.readValue(jsonText, ThumbnailRequestModel.class);
        } catch (IOException e) {
            e.printStackTrace();
            responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
            return responseModel.buildResponse();
        }

        //System.err.println("CheckPoint : " + requestModel.getMovie_ids());

        if (requestModel.getMovie_ids().length < 1) {
            responseModel.setResult(Result.NO_MOVIE_FOUND);
            return responseModel.buildResponse();
        }

        ThumbnailModel[] thumbnails = Movie.getMovieThumbnails(requestModel.getMovie_ids());

        if (thumbnails.length < 1 || thumbnails == null) {
            responseModel.setResult(Result.NO_MOVIE_FOUND);
        } else {
            responseModel.setThumbnails(thumbnails);
            responseModel.setResult(Result.FOUND_MOVIES);
        }
        return responseModel.buildResponse();
    }

}
