package edu.uci.ics.dillonhl.service.billing.resources.part2;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.dillonhl.service.billing.base.Result;
import edu.uci.ics.dillonhl.service.billing.base.itemModel;
import edu.uci.ics.dillonhl.service.billing.base.transactionModel;
import edu.uci.ics.dillonhl.service.billing.core.Order;
import edu.uci.ics.dillonhl.service.billing.core.PayPal;
import edu.uci.ics.dillonhl.service.billing.core.PayPalOrderClient;
import edu.uci.ics.dillonhl.service.billing.models.EmailRequestModel;
import edu.uci.ics.dillonhl.service.billing.models.OrderPlaceResponseModel;
import edu.uci.ics.dillonhl.service.billing.models.OrderRetrieveResponseModel;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("order/retrieve")
public class OrderRetrievePage {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Response orderPlace(@Context HttpHeaders headers, String jsonString) {

        EmailRequestModel requestModel;
        OrderRetrieveResponseModel responseModel = new OrderRetrieveResponseModel(); // will have to change to show all items retrieved
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

        PayPalOrderClient client = new PayPalOrderClient();

        String token = Order.getOrderID(requestModel.getEmail());

        try {
            String response = PayPal.getOrder(token);
            JsonNode json = mapper.readTree(response);

            transactionModel transactions = new transactionModel();
            itemModel[] items = { new itemModel()};
            transactions.setCapture_id(json.get("id"));
            transactions.setState("completed");
            transactions.setAmount(json.get("amount"));
            System.err.println( "\n check\n\n\n" + json.get("purchase_units") + "\n\n\n\n");
            //System.err.println( "\n check\n\n\n" + json.get("purchase_units") + "\n\n\n\n");


            transactions.setCreate_time(json.get("create_time"));
            transactions.setUpdate_time(json.get("update_time"));
            transactions.setItems(items);
            responseModel.setTransactions(transactions);
            responseModel.setResult(Result.ORDER_RETRIEVED_SUCCESSFUL);
        } catch (IOException e)
        {
            System.err.println("Could not get order.");
            responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
        }

        return responseModel.buildResponse();
    }
}
