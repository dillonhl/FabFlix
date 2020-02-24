package edu.uci.ics.dillonhl.service.idm.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.dillonhl.service.idm.base.Result;
import edu.uci.ics.dillonhl.service.idm.core.RegisterUser;
import edu.uci.ics.dillonhl.service.idm.core.SearchUserByEmail;
import edu.uci.ics.dillonhl.service.idm.logger.ServiceLogger;
import edu.uci.ics.dillonhl.service.idm.models.EmailAndPassRequestModel;
import edu.uci.ics.dillonhl.service.idm.models.EmailAndPassResponseModel;
import edu.uci.ics.dillonhl.service.idm.models.GeneralResponseModel;
import edu.uci.ics.dillonhl.service.idm.models.LoginResponseModel;
import edu.uci.ics.dillonhl.service.idm.security.Crypto;
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
import static edu.uci.ics.dillonhl.service.idm.IDMService.passwordIsValid;

@Path("register")
public class RegisterPage {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(@Context HttpHeaders headers, String jsonText)
    {
        System.err.println("Register start...");
        EmailAndPassRequestModel requestModel;
        EmailAndPassResponseModel responseModel;
        ObjectMapper mapper = new ObjectMapper();
        System.err.println("Headers: " + headers.getHeaderString("email"));

        try {
            requestModel = mapper.readValue(jsonText, EmailAndPassRequestModel.class);
        } catch (IOException e)
        {
            e.printStackTrace();
            if (e instanceof JsonMappingException){
                responseModel = new EmailAndPassResponseModel(-2,"JSON Mapping Exception.");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            else if (e instanceof JsonParseException) {
                responseModel= new EmailAndPassResponseModel(-3,"JSON Parse Exception.");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            else
            {
                responseModel = new EmailAndPassResponseModel(-1, "Internal server error.");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
            }
        }

        ServiceLogger.LOGGER.info("Received request for register.");
        ServiceLogger.LOGGER.info("Request: \n" + jsonText);

        // Check email and password length/character requirements
        if (requestModel.getEmail() == null || requestModel.getEmail() == "")
        {
            responseModel = new EmailAndPassResponseModel(-10, "Email address invalid length.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }
        else if (requestModel.getPassword() == null || requestModel.getPassword().length == 0)
        {
            responseModel = new EmailAndPassResponseModel(-12, "Password has invalid length.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }
        else if (!emailIsValid(requestModel.getEmail()))
        {
            responseModel = new EmailAndPassResponseModel(-11, "Email address invalid format.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }
        else if (passwordIsValid(requestModel.getPassword()) == 12)
        {
            responseModel = new EmailAndPassResponseModel(12, "Password does not meet length requirements.");
            return Response.status(Response.Status.OK).entity(responseModel).build();
        }
        else if (passwordIsValid(requestModel.getPassword()) == 13)
        {
            responseModel = new EmailAndPassResponseModel(13, "Password does not meet character requirements.");
            return Response.status(Response.Status.OK).entity(responseModel).build();
        }

        System.err.println("Password is valid...");

        // Search user db to check if email is in use already
        if (SearchUserByEmail.searchPLevelByEmail(requestModel.getEmail()) != 0)
        {
            responseModel = new EmailAndPassResponseModel(16, "Email already in use.");
            return Response.status(Response.Status.OK).entity(responseModel).build();
        }


        // salt and hash
        byte[] salt = Crypto.genSalt();
        System.err.println("Salt: " + salt);

        char[] password = requestModel.getPassword();
        byte[] hashedPassword = Crypto.hashPassword(password, salt, Crypto.ITERATIONS, Crypto.KEY_LENGTH);

        String encodedSalt = Hex.encodeHexString(salt);
        String encodedPassword = Hex.encodeHexString(hashedPassword);

        RegisterUser.registerUserIntoDB(requestModel.getEmail(), encodedPassword, encodedSalt);

        //responseModel = new EmailAndPassResponseModel(110, "User registered successfully.");
        //return Response.status(Response.Status.OK).entity(responseModel).build();
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        generalResponseModel.setResult(Result.USER_REGISTERED_SUCCESS);

        return generalResponseModel.buildResponse();

        /*System.err.println("Headers: \nemail: " + headers.getHeaderString("email") + "\ntransaction_id: " + headers.getHeaderString("transaction_id"));

        return generalResponseModel.buildResponse(requestModel.getEmail(),
                headers.getHeaderString("transaction_id"), headers.getHeaderString("session_id"));*/
    }




}
