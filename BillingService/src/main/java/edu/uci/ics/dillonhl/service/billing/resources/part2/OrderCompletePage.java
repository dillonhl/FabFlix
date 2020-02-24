package edu.uci.ics.dillonhl.service.billing.resources.part2;

import edu.uci.ics.dillonhl.service.billing.base.Result;
import edu.uci.ics.dillonhl.service.billing.core.Cart;
import edu.uci.ics.dillonhl.service.billing.core.Order;
import edu.uci.ics.dillonhl.service.billing.core.PayPal;
import edu.uci.ics.dillonhl.service.billing.core.PayPalOrderClient;
import edu.uci.ics.dillonhl.service.billing.models.GeneralResponseModel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;

import static edu.uci.ics.dillonhl.service.billing.core.PayPal.captureOrder;

@Path("order/complete")
public class OrderCompletePage {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response orderComplete(@Context HttpHeaders headers, @QueryParam("token") String token, @QueryParam("payer_id") String payer_id)
    {
        GeneralResponseModel responseModel = new GeneralResponseModel();
        PayPalOrderClient client = new PayPalOrderClient();

        String email = Order.checkToken(token);
        if (email == null)
        {
            responseModel.setResult(Result.TOKEN_NOT_FOUND);
        }

        String capture_id = PayPal.captureOrder(token);

        if (capture_id == "Server Error")
        {
            responseModel.setResult(Result.INTERNAL_SERVER_ERROR);
        }
        else if (capture_id == "Client Error")
        {
            responseModel.setResult(Result.ORDER_NOT_COMPLETED);
        }
        else
        {
            Order.updateTrans(capture_id, token);
            responseModel.setResult(Result.ORDER_COMPLETED_SUCCESSFUL);
            Cart.clearCart(email);
        }

        try {
            PayPal.getOrder(token);
        } catch (IOException e)
        {
            System.err.println("Could not get order.");
        }

        return responseModel.buildResponse();

    }
}
