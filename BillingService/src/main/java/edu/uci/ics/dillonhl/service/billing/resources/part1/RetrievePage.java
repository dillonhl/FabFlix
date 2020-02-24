package edu.uci.ics.dillonhl.service.billing.resources.part1;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.dillonhl.service.billing.base.Result;
import edu.uci.ics.dillonhl.service.billing.base.itemModel;
import edu.uci.ics.dillonhl.service.billing.core.Cart;
import edu.uci.ics.dillonhl.service.billing.models.EmailRequestModel;
import edu.uci.ics.dillonhl.service.billing.models.RetrieveResponseModel;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("cart/retrieve")
public class RetrievePage {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Response retrieveCart(@Context HttpHeaders headers, String jsonString) {

        EmailRequestModel requestModel;
        RetrieveResponseModel responseModel = new RetrieveResponseModel(); // will have to change to show all items retrieved
        ObjectMapper mapper = new ObjectMapper();

        try {
            requestModel = mapper.readValue(jsonString, EmailRequestModel.class);
        } catch (IOException e) {
            if (e instanceof JsonMappingException) {
                responseModel.setResult(Result.JSON_MAPPING_EXCEPTION);
                return responseModel.buildResponse();
            } else if (e instanceof JsonParseException) {
                responseModel.setResult(Result.JSON_PARSE_EXCEPTION);
                return responseModel.buildResponse();
            } else {
                responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
                return responseModel.buildResponse();
            }
        }

        // retrieve cart,  // if no items in cart return resultCode 312
        System.err.println("GETTING ITEMS");
        itemModel[] items = Cart.retrieveCart(requestModel.getEmail());

        System.err.println("ITEMS: " + items);

        if (items == null || items.length == 0)
        {
            responseModel.setItems(null);
            responseModel.setResult(Result.NONEXISTENT_ITEM);
        }
        else
        {
            responseModel.setItems(items);
            responseModel.setResult(Result.RETRIEVED_SUCCESSFUL);
        }

        //System.err.println("RESPONSE MODEL: " + responseModel.getResultCode() + responseModel.getMessage() + responseModel.getItems());

        return responseModel.buildResponse();
    }
}