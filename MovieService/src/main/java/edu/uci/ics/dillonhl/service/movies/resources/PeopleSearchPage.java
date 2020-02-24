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
import edu.uci.ics.dillonhl.service.movies.core.Person;
import edu.uci.ics.dillonhl.service.movies.logger.ServiceLogger;
import edu.uci.ics.dillonhl.service.movies.models.MovieSearchResponseModel;
import edu.uci.ics.dillonhl.service.movies.models.PeopleResponseModel;
//import edu.uci.ics.dillonhl.service.movies.models.PersonResponseModel;
import edu.uci.ics.dillonhl.service.movies.models.PrivilegeRequestModel;
import edu.uci.ics.dillonhl.service.movies.models.data.PersonModel;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.*;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
@Path("people/search")
public class PeopleSearchPage {
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response privilege(@Context HttpHeaders headers, @QueryParam("name") String name,
                              @QueryParam("birthday") String birthday, @QueryParam("movie_title") String title,
                              @QueryParam("limit") Integer limit, @QueryParam("offset") Integer offset,
                              @QueryParam("orderby") String orderby, @QueryParam("direction") String direction) {
        //header strings
        String email = headers.getHeaderString("email");
        String session_id = headers.getHeaderString("session_id");
        String transaction_id = headers.getHeaderString("transaction_id");

        System.err.println("Email: " + email + "\nSession_id: " + session_id + "\nTransaction_id: " + transaction_id);

        PeopleResponseModel responseModel = new PeopleResponseModel();

        ServiceLogger.LOGGER.info("Received request to search people");

        System.err.println("Query params: " + "Name: " + name
                + " BDAY: " + birthday +  " Title: " + title + " Limit: " + limit + " Offset: " + offset +
                " Orderyby: " + orderby + " direction: " + direction);

        if (limit == null || (limit != 10 && limit != 25 && limit != 50 && limit != 100))
            limit = 10;
        if (offset == null || offset < 0 || offset % 5 != 0)
            offset = 0;
        if (orderby == null || orderby.isEmpty()) // check for only 3 options as well
            orderby = "name";
        if (direction == null || direction.isEmpty())
            direction = "asc";

        String query = Person.buildPeopleQuery(name, birthday, title, limit, offset, orderby, direction);

        PersonModel[] people = Person.searchPeople(query);

        if (people == null)
        {
            responseModel.setResult(Result.NO_PEOPLE_FOUND);
        }
        else if (people.length == 0)
        {
            responseModel.setResult(Result.NO_PEOPLE_FOUND);
        }
        else
        {
            responseModel.setPeople(people);
            responseModel.setResult(Result.FOUND_PEOPLE);
        }

        return responseModel.buildResponse();
    }
}
