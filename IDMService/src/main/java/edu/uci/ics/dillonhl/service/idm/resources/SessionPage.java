package edu.uci.ics.dillonhl.service.idm.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.dillonhl.service.idm.base.Result;
import edu.uci.ics.dillonhl.service.idm.core.InsertSession;
import edu.uci.ics.dillonhl.service.idm.core.SearchSession;
import edu.uci.ics.dillonhl.service.idm.core.SearchUserByEmail;
import edu.uci.ics.dillonhl.service.idm.logger.ServiceLogger;
import edu.uci.ics.dillonhl.service.idm.models.*;
import edu.uci.ics.dillonhl.service.idm.security.Crypto;
import edu.uci.ics.dillonhl.service.idm.security.Session;
import jdk.nashorn.internal.ir.SetSplitState;
import org.apache.commons.codec.binary.Hex;
import org.checkerframework.checker.units.qual.C;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.io.IOException;
import java.util.zip.CRC32;

import static edu.uci.ics.dillonhl.service.idm.IDMService.emailIsValid;

@Path("session")
public class SessionPage {
    //@Path("session")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Response createSession(@Context HttpHeaders headers, String jsonText)
    {
        SessionRequestModel requestModel;
        SessionResponseModel responseModel = new SessionResponseModel();
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestModel = mapper.readValue(jsonText, SessionRequestModel.class);
        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException){
                //responseModel = new SessionResponseModel(-2,"JSON Mapping Exception.", null);
                //return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
                responseModel.setResult(Result.JSON_MAPPING_EXCEPTION);
                return responseModel.buildResponse();
            }
            else if (e instanceof JsonParseException) {
                //responseModel = new SessionResponseModel(-3,"JSON Parse Exception.", null);
                //return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
                responseModel.setResult(Result.JSON_PARSE_EXCEPTION);
                return responseModel.buildResponse();
            }
            else
            {
                responseModel = new SessionResponseModel(-1, "Internal server error.", null);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
            }
        }

        ServiceLogger.LOGGER.info("Received request for session");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);

        if (requestModel.getEmail() == null || requestModel.getEmail().equals("") || requestModel.getEmail().isEmpty())
        {
            //responseModel = new SessionResponseModel(-10, "Email address invalid length.", null);
            //return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            responseModel.setResult(Result.INVALID_EMAIL_LENGTH);
            return responseModel.buildResponse();
        }
        else if (!emailIsValid(requestModel.getEmail()))
        {
            //responseModel = new SessionResponseModel(-11, "Email address invalid format.", null);
            //return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            responseModel.setResult(Result.INVALID_EMAIL_FORMAT);
            return responseModel.buildResponse();
        }

        if (requestModel.getSession_id().length() > 128 || requestModel.getSession_id().length() < 128 ||
            requestModel.getSession_id().isEmpty() || requestModel.getSession_id() == null || requestModel.getSession_id().length() == 0)
        {
            responseModel.setResult(Result.INVALID_TOKEN_LENGTH);
            return responseModel.buildResponse();
        }

        int userSearch = SearchUserByEmail.searchPLevelByEmail(requestModel.getEmail()); // just checks if there's a user
        if (userSearch == 0)
        {
            ServiceLogger.LOGGER.info("User: " + requestModel.getEmail() + " not found.");
            //responseModel = new SessionResponseModel(14, "User not found.", "");
            //return Response.status(Response.Status.OK).entity(responseModel).build();
            responseModel.setResult(Result.USER_NOT_FOUND);
            return responseModel.buildResponse();
        }
        int sessionRequest = SearchSession.getSessionStatus(requestModel.getSession_id());

        if (sessionRequest == 0)
        {
            //responseModel = new SessionResponseModel(134, "Session not found.", requestModel.getSession_id());
            responseModel.setResult(Result.SESSION_NOT_FOUND);
            //return Response.status(Response.Status.OK).entity(responseModel).build();
        }
        else if (sessionRequest == 1)
        {
            responseModel.setResult(Result.SESSION_ACTIVE);
            responseModel.setSession_id(requestModel.getSession_id());
        }
        else if (sessionRequest == 2)
        {
            responseModel.setResult(Result.SESSION_CLOSED);
            //responseModel.setSession_id(requestModel.getSession_id());
        }
        else if (sessionRequest == 3)
        {
            responseModel.setResult(Result.SESSION_EXPIRED);
            //responseModel.setSession_id(requestModel.getSession_id());
        }
        else if (sessionRequest == 4)
        {
            responseModel.setResult(Result.SESSION_REVOKED);
            //responseModel.setSession_id(requestModel.getSession_id());
        }
        else if (sessionRequest == 5) // what is supposed to be returned?
        {
            Session session = Session.createSession(requestModel.getEmail());
            InsertSession.insertNewSession(session.getSessionID().toString(), session.getEmail());
            responseModel.setResult(Result.SESSION_ACTIVE);
            responseModel.setSession_id(session.getSessionID().toString());
        }

        return responseModel.buildResponse();

    }
}
