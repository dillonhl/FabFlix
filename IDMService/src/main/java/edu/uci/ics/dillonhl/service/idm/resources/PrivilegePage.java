package edu.uci.ics.dillonhl.service.idm.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.dillonhl.service.idm.base.Result;
import edu.uci.ics.dillonhl.service.idm.core.SearchUserByEmail;
import edu.uci.ics.dillonhl.service.idm.logger.ServiceLogger;
import edu.uci.ics.dillonhl.service.idm.models.GeneralResponseModel;
import edu.uci.ics.dillonhl.service.idm.models.LoginResponseModel;
import edu.uci.ics.dillonhl.service.idm.models.PrivilegeRequestModel;
import edu.uci.ics.dillonhl.service.idm.models.PrivilegeResponseModel;

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

@Path("privilege")
public class PrivilegePage {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response privilege(@Context HttpHeaders headers, String jsonText)
    {
        System.err.println("Header email: " + headers.getHeaderString("email"));
        PrivilegeRequestModel requestModel;
        GeneralResponseModel responseModel = new GeneralResponseModel();
        ObjectMapper mapper = new ObjectMapper();

        try {
            requestModel = mapper.readValue(jsonText, PrivilegeRequestModel.class);
        } catch (IOException e)
        {
            e.printStackTrace();
            if (e instanceof JsonMappingException){
                //responseModel = new PrivilegeResponseModel(-2,"JSON Mapping Exception.");
                //return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
                responseModel.setResult(Result.JSON_MAPPING_EXCEPTION);
            }
            else if (e instanceof JsonParseException) {
                //responseModel= new PrivilegeResponseModel(-3,"JSON Parse Exception.");
                //return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
                responseModel.setResult(Result.JSON_PARSE_EXCEPTION);
            }
            else
            {
                responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
                //responseModel = new PrivilegeResponseModel(-1, "Internal server error.");
                //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
            }
            return responseModel.buildResponse();
        }

        ServiceLogger.LOGGER.info("Received request to check privilege level");
        ServiceLogger.LOGGER.info("Request: \n " + jsonText);

        if (requestModel.getEmail() == null || requestModel.getEmail() == "")
        {
            responseModel.setResult(Result.INVALID_EMAIL_LENGTH);
            return responseModel.buildResponse();
            //responseModel = new PrivilegeResponseModel(-10, "Email address invalid length.");
            //return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }
        else if (!emailIsValid(requestModel.getEmail()))
        {
            responseModel.setResult(Result.INVALID_EMAIL_FORMAT);
            return responseModel.buildResponse();
            //responseModel = new PrivilegeResponseModel(-11, "Email address invalid format.");
            //return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }
        else if (requestModel.getPLevel() < 1 || requestModel.getPLevel() > 5)
        {
            responseModel.setResult(Result.PRIVILEGE_OUT_OF_RANGE);
            return responseModel.buildResponse();
            //responseModel = new PrivilegeResponseModel(-14, "Privilege level out of valid range.");
            //return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }

        int request = SearchUserByEmail.searchPLevelByEmail(requestModel.getEmail());
        System.err.println("PLevel Request : " + request);
        if (request == 0)
        {
            responseModel.setResult(Result.USER_NOT_FOUND);
            //responseModel = new PrivilegeResponseModel(14, "User not found.");
        }
        else if (requestModel.getPLevel() >= request) // changed
        {
            responseModel.setResult(Result.USER_SUFFICIENT_PLEVEL);
            //responseModel = new PrivilegeResponseModel(140, "User has sufficient privilege level.");
        }
        else
        {
            responseModel.setResult(Result.USER_INSUFFICIENT_PLEVEL);
            //responseModel = new PrivilegeResponseModel(141, "User has insufficient privilege level.");
        }

        System.err.println("Response being built...");
        //return Response.status(Response.Status.OK).entity(responseModel).build();
        return responseModel.buildResponse();
    }

}
