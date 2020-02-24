package edu.uci.ics.dillonhl.service.idm.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.dillonhl.service.idm.base.Result;
import edu.uci.ics.dillonhl.service.idm.core.InsertSession;
import edu.uci.ics.dillonhl.service.idm.core.SearchSession;
import edu.uci.ics.dillonhl.service.idm.core.UserLogin;
import edu.uci.ics.dillonhl.service.idm.logger.ServiceLogger;
import edu.uci.ics.dillonhl.service.idm.models.EmailAndPassRequestModel;
import edu.uci.ics.dillonhl.service.idm.models.EmailAndPassResponseModel;
import edu.uci.ics.dillonhl.service.idm.models.LoginResponseModel;
import edu.uci.ics.dillonhl.service.idm.security.Crypto;
import edu.uci.ics.dillonhl.service.idm.security.Session;
import org.apache.commons.codec.binary.Hex;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static edu.uci.ics.dillonhl.service.idm.IDMService.emailIsValid;

@Path("login")
public class LoginPage {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response privilege(@Context HttpHeaders headers, String jsonText) {
        System.err.println("Starting login...");
        EmailAndPassRequestModel requestModel;
        LoginResponseModel responseModel = new LoginResponseModel();
        ObjectMapper mapper = new ObjectMapper();

        try {
            requestModel = mapper.readValue(jsonText, EmailAndPassRequestModel.class);
        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException) {
                responseModel = new LoginResponseModel(-2, "JSON Mapping Exception.", null);
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            } else if (e instanceof JsonParseException) {
                responseModel = new LoginResponseModel(-3, "JSON Parse Exception.", null);
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            } else {
                responseModel = new LoginResponseModel(-1, "Internal server error.", null);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
            }
        }

        ServiceLogger.LOGGER.info("Received request to login");
        ServiceLogger.LOGGER.info("Request: \n " + jsonText);

        if (requestModel.getEmail() == null || requestModel.getEmail() == "")
        {
            responseModel = new LoginResponseModel(-10, "Email address invalid length.", null);
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }
        else if (requestModel.getPassword() == null || requestModel.getPassword().length == 0)
        {
            responseModel = new LoginResponseModel(-12, "Password has invalid length.", null);
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }
        else if (!emailIsValid(requestModel.getEmail()))
        {
            responseModel = new LoginResponseModel(-11, "Email address invalid format.", null);
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }


        Integer loginRequest = UserLogin.login(requestModel.getEmail(), requestModel.getPassword());
        if (loginRequest == -1)
        {
            System.err.println("User not found.");
            responseModel = new LoginResponseModel(14, "User not found.", null);
            return Response.status(Response.Status.OK).entity(responseModel).build();
            //responseModel.setResult(Result.USER_NOT_FOUND);
            //return responseModel.buildResponse(headers.getHeaderString("email"), headers.getHeaderString("transaction_id"), headers.getHeaderString("session_id"));
        }
        else if (loginRequest == 0)
        {
            System.err.println("Email and password are not correct.");
            responseModel = new LoginResponseModel(11, "Passwords do not match.", null);
            return Response.status(Response.Status.OK).entity(responseModel).build();
        }
        else
        {
            if (SearchSession.searchSession(requestModel.getEmail()) == 1) {
                SearchSession.updateSession(requestModel.getEmail(), SearchSession.searchSessionID(requestModel.getEmail()), 4); //REVOKE SESSION
            }

            Session session = Session.createSession(requestModel.getEmail());
            InsertSession.insertNewSession(session.getSessionID().toString(), session.getEmail());
            responseModel = new LoginResponseModel(120, "User logged in successfully.", session.getSessionID().toString());
            return Response.status(Response.Status.OK).entity(responseModel).build();
        }

    }


}
