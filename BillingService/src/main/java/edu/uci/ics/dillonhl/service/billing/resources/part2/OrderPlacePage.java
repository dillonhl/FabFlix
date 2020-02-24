package edu.uci.ics.dillonhl.service.billing.resources.part2;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import edu.uci.ics.dillonhl.service.billing.base.Result;
import edu.uci.ics.dillonhl.service.billing.core.Cart;
import edu.uci.ics.dillonhl.service.billing.core.Order;
import edu.uci.ics.dillonhl.service.billing.core.PayPal;
import edu.uci.ics.dillonhl.service.billing.core.PayPalOrderClient;
import edu.uci.ics.dillonhl.service.billing.models.EmailRequestModel;
import edu.uci.ics.dillonhl.service.billing.models.OrderPlaceResponseModel;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("order/place")
public class OrderPlacePage {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Response orderPlace(@Context HttpHeaders headers, String jsonString) {

        EmailRequestModel requestModel;
        OrderPlaceResponseModel responseModel = new OrderPlaceResponseModel(); // will have to change to show all items retrieved
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

        if (!Cart.checkCart(requestModel.getEmail())) {
            responseModel.setResult(Result.NONEXISTENT_ITEM);
            return responseModel.buildResponse();
        }

        float total = Order.getCartTotal(requestModel.getEmail());

        PayPalOrderClient client = new PayPalOrderClient();

        String token = PayPal.createPayPalOrder(client, total);

        System.err.println("Token from createPayPalOrder: " + token);

        if (token == null)
            responseModel.setResult(Result.ORDER_CREATION_FAILED);
        else {
            if (Order.insertSaleAndTransaction(requestModel.getEmail(), token))
                responseModel.setResult(Result.ORDER_PLACED_SUCCESSFUL);
            else
                responseModel.setResult(Result.ORDER_CREATION_FAILED);
            responseModel.setToken(token);
            responseModel.setApprove_url("https://www.sandbox.paypal.com/checkoutnow?token=" + token);
        }

        return responseModel.buildResponse();
    }


}

