package edu.uci.ics.dillonhl.service.billing.resources.part1;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.dillonhl.service.billing.BillingService;
import edu.uci.ics.dillonhl.service.billing.base.Result;
import edu.uci.ics.dillonhl.service.billing.configs.IdmConfigs;
import edu.uci.ics.dillonhl.service.billing.core.Cart;
import edu.uci.ics.dillonhl.service.billing.logger.ServiceLogger;
import edu.uci.ics.dillonhl.service.billing.models.EMQRequestModel;
import edu.uci.ics.dillonhl.service.billing.models.GeneralResponseModel;
import edu.uci.ics.dillonhl.service.billing.models.PrivilegeRequestModel;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("cart/insert")
public class InsertPage {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Response insertCart(@Context HttpHeaders headers, String jsonString) {

        //Contact IDM service // use config file
        IdmConfigs idm = BillingService.getIdmConfigs();
        //String servicePath = "http://localhost:5333/api/idm";
        String servicePath = idm.getScheme() + idm.getHostName() + ":" + idm.getPort() + idm.getPath();
        String endpointPath = idm.getPrivilegePath();
        System.err.println("PATH: " + servicePath + endpointPath);
        //String endpointPath = "/privilege";
        PrivilegeRequestModel pRequestModel;
        EMQRequestModel requestModel;// = new EMQRequestModel();
        GeneralResponseModel responseModel = new GeneralResponseModel();
        ObjectMapper mapper = new ObjectMapper();

        //ServiceLogger.LOGGER.info("Creating request Model");
        try {
            requestModel = mapper.readValue(jsonString, EMQRequestModel.class);
        } catch (IOException e) {
            if (e instanceof JsonMappingException) {
                responseModel.setResult(Result.JSON_MAPPING_EXCEPTION);
                return responseModel.buildResponse();
            } else if (e instanceof JsonParseException){
                responseModel.setResult(Result.JSON_PARSE_EXCEPTION);
                return responseModel.buildResponse();
            } else
            {
                responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
                return responseModel.buildResponse();
            }
        }

        pRequestModel = new PrivilegeRequestModel(requestModel.getEmail(), 4);

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
        Response response = invocationBuilder.post(Entity.entity(pRequestModel, MediaType.APPLICATION_JSON));
        ServiceLogger.LOGGER.info("Request sent.");

        ServiceLogger.LOGGER.info("Received status " + response.getStatus());

        int pResult;
        try {
            String jsonText = response.readEntity(String.class);
            System.err.println("jsonText in response: " + jsonText);

            JSONObject jsonObject = new JSONObject(jsonText);

            pResult = jsonObject.getInt("resultCode");
        } catch (JSONException e)
        {
            pResult = 14;
        }

        if (pResult == 14) // correct this because status is not result code from privilege
        {
            ServiceLogger.LOGGER.info("User not found.");
            responseModel.setResult(Result.USER_NOT_FOUND);
            return responseModel.buildResponse();
        }

        if (requestModel.getQuantity() < 1) // quantity
        {
            responseModel.setResult(Result.INVALID_QUANTITY);
            return responseModel.buildResponse();
        }

        //CHECK IF MOVIE_ID HAS A PRICE IN THE DATABASE
        if (!Cart.validItem(requestModel.getMovie_id()))
        {
            responseModel.setResult(Result.OPERATION_FAILED);
            return responseModel.buildResponse();
        }

        // check if cart item is duplicate
        if (Cart.checkItem(requestModel.getEmail(), requestModel.getMovie_id()))
        {
            responseModel.setResult(Result.DUPLICATE_INSERTION);
        }
        else
        {
            responseModel.setResult(Cart.insert(requestModel.getEmail(), requestModel.getMovie_id(), requestModel.getQuantity()));
        }

        return responseModel.buildResponse();

    }

}
