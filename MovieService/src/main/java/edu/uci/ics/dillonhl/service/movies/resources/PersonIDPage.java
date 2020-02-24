package edu.uci.ics.dillonhl.service.movies.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.dillonhl.service.movies.MoviesService;
import edu.uci.ics.dillonhl.service.movies.base.GenResponseModel;
import edu.uci.ics.dillonhl.service.movies.base.Result;
import edu.uci.ics.dillonhl.service.movies.configs.IdmConfigs;
import edu.uci.ics.dillonhl.service.movies.core.Person;
import edu.uci.ics.dillonhl.service.movies.logger.ServiceLogger;
import edu.uci.ics.dillonhl.service.movies.models.PersonIDResponseModel;
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

//import edu.uci.ics.dillonhl.service.movies.models.PersonResponseModel;



@Path("/")
public class PersonIDPage {
    @Path("people/get/{person_id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response privilege(@PathParam("person_id") int person_id, @Context HttpHeaders headers) {
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
        PersonIDResponseModel responseModel = new PersonIDResponseModel();

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
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        }
        int pResult = genResponseModel.getResultCode();

        ServiceLogger.LOGGER.info("Received request to search person with id " + person_id);


        PersonModel person = Person.searchPersonID(person_id);

        if (person == null)
        {
            responseModel.setResult(Result.NO_PEOPLE_FOUND);
        }
        else {
            responseModel.setPerson(person);
            responseModel.setResult(Result.FOUND_PEOPLE);
        }

        return responseModel.buildResponse();
    }
}
