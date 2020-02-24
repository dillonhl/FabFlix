package edu.uci.ics.dillonhl.service.billing.resources.part1;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.dillonhl.service.billing.base.Result;
import edu.uci.ics.dillonhl.service.billing.core.Cart;
import edu.uci.ics.dillonhl.service.billing.models.EMRequestModel;
import edu.uci.ics.dillonhl.service.billing.models.GeneralResponseModel;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("cart/delete")
public class DeletePage {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Response deleteFromCart(@Context HttpHeaders headers, String jsonString) {

        EMRequestModel requestModel;
        GeneralResponseModel responseModel = new GeneralResponseModel();
        ObjectMapper mapper = new ObjectMapper();

        try {

            requestModel = mapper.readValue(jsonString, EMRequestModel.class);
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

        // check if cart item is in cart table
        if (!Cart.checkItem(requestModel.getEmail(), requestModel.getMovie_id())) {
            responseModel.setResult(Result.NONEXISTENT_ITEM);
        } else {

            responseModel.setResult(Cart.deleteItem(requestModel.getEmail(), requestModel.getMovie_id()));
        }


        return responseModel.buildResponse();
    }
}
