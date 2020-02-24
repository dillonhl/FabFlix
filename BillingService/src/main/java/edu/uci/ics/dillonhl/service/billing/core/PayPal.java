package edu.uci.ics.dillonhl.service.billing.core;

import com.braintreepayments.http.serializer.Json;
import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.braintreepayments.http.HttpResponse;
import com.braintreepayments.http.exceptions.HttpException;
import com.paypal.orders.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.paypal.orders.Order;
import org.json.JSONObject;

public class PayPal {

    private static final String clientId = "AS1njKCVkFkqGlS2nG-nyOZOuePnRbykD96YoUjzX__Zya0QuNuJGkwqFqlGfLK5djMwV8CrsN0-tWib";
    private static final String clientSecret = "EIOH8nYu2rv9-ZhFuD20-oK58JpQYvrdrSv_U1EfhJWEy_pE8H_ycYya-vZcsqukYj2jBvqK82Tirw1U";

    //setup paypal envrionment
    public static PayPalEnvironment environment = new PayPalEnvironment.Sandbox(clientId, clientSecret);
    //Create client for environment
    private static PayPalHttpClient client = new PayPalHttpClient(environment);

    public static String createPayPalOrder(PayPalOrderClient client, float total)
    {
        com.paypal.orders.Order order = null;

        //Construct a request object and set desired parameters
        //Here orderscreaterequest creates a post request to v2/checkout/orders

        OrderRequest orderRequest = new OrderRequest();

        //MUST use this method instead of intent to create capture.
        orderRequest.checkoutPaymentIntent("CAPTURE");

        //Create application context with return url upon payer completion.
        ApplicationContext applicationContext = new ApplicationContext().returnUrl("http://localhost:5332/api/billing/order/complete")
                .cancelUrl("http://localhost:5332/api/billing/order/place");


        orderRequest.applicationContext(applicationContext);

        List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
        purchaseUnits
                .add(new PurchaseUnitRequest().amountWithBreakdown(new AmountWithBreakdown().currencyCode("USD").value(String.format("%.2f", total))));
        orderRequest.purchaseUnits(purchaseUnits);
        OrdersCreateRequest request = new OrdersCreateRequest().requestBody(orderRequest);

        try {
            // Call API with your client and get a response for your call
            HttpResponse<com.paypal.orders.Order> response = client.client.execute(request);

            // If call returns body in response, you can get the de-serialized version by
            // calling result() on the response
            order = response.result();
            System.out.println("Order ID: " + order.id());
            order.links().forEach(link -> System.out.println(link.rel() + " => " + link.method() + ":" + link.href()));
            return order.id();
        } catch (IOException ioe) {
            System.err.println("*******COULD NOT CREATE ORDER*******");
            if (ioe instanceof HttpException) {
                // Something went wrong server-side
                HttpException he = (HttpException) ioe;
                System.out.println(he.getMessage());
                he.headers().forEach(x -> System.out.println(x + " :" + he.headers().header(x)));
            } else {


                // Something went wrong client-side
            }
            return null;
        }

    }
    public String captureOrder(String orderID, PayPalOrderClient orderClient)
    {
        com.paypal.orders.Order order = null;
        OrdersCaptureRequest request = new OrdersCaptureRequest(orderID);

        try {

            // Call API with your client and get a response for your call
            HttpResponse<com.paypal.orders.Order> response = orderClient.client.execute(request);

            // If call returns body in response, you can get the de-serialized version by
            // calling result() on the response
            order = response.result();


            System.out.println("Capture ID: " + order.purchaseUnits().get(0).payments().captures().get(0).id());

            order.purchaseUnits().get(0).payments().captures().get(0).links()
                    .forEach(link -> System.out.println(link.rel() + " => " + link.method() + ":" + link.href()));

            return order.purchaseUnits().get(0).payments().captures().get(0).id();
        } catch (IOException ioe) {
            if (ioe instanceof HttpException) {
                // Something went wrong server-side
                HttpException he = (HttpException) ioe;
                System.out.println(he.getMessage());
                he.headers().forEach(x -> System.out.println(x + " :" + he.headers().header(x)));
                return "serverError";
            } else {
                // Something went wrong client-side
                return "clientError";
            }
        }

    }

    public void getOrder(String orderId, PayPalOrderClient orderClient) throws IOException {
        OrdersGetRequest request = new OrdersGetRequest(orderId);
        HttpResponse<com.paypal.orders.Order> response = orderClient.client.execute(request);
        System.out.println("Full response body:" + (new Json().serialize(response.result())));
        // System.out.println(new JSONObject(new Json().serialize(response.result())).toString(4));
    }

    public static String captureOrder(String orderID)
    {
        com.paypal.orders.Order order = null;
        OrdersCaptureRequest request = new OrdersCaptureRequest(orderID);

        try {

            // Call API with your client and get a response for your call
            HttpResponse<com.paypal.orders.Order> response = client.execute(request);

            // If call returns body in response, you can get the de-serialized version by
            // calling result() on the response
            order = response.result();
            System.out.println("Payer ID: " + order.payer().payerId());

            System.out.println("Capture ID: " + order.purchaseUnits().get(0).payments().captures().get(0).id());
            order.payer().payerId();
            order.purchaseUnits().get(0).payments().captures().get(0).links()
                    .forEach(link -> System.out.println(link.rel() + " => " + link.method() + ":" + link.href()));

            return order.purchaseUnits().get(0).payments().captures().get(0).id();
        } catch (IOException ioe) {
            if (ioe instanceof HttpException) {
                // Something went wrong server-side
                HttpException he = (HttpException) ioe;
                System.out.println(he.getMessage());
                he.headers().forEach(x -> System.out.println(x + " :" + he.headers().header(x)));
                return "Server Error";
            } else {
                // Something went wrong client-side
                return "Client Error";
            }
        }

    }

    /**
     * Method to perform sample GET on an order
     *
     * @throws IOException Exceptions from API if any
     */
    public static String getOrder(String orderId) throws IOException {
        OrdersGetRequest request = new OrdersGetRequest(orderId);
        HttpResponse<Order> response = client.execute(request);
        System.out.println("Full response body:" + (new Json().serialize(response.result())));
        return (new Json().serialize(response.result()));
        // System.out.println(new JSONObject(new Json().serialize(response.result())).toString(4));
    }
}

/*class PayPalOrderClient

{
    private final String clientId = "AS1njKCVkFkqGlS2nG-nyOZOuePnRbykD96YoUjzX__Zya0QuNuJGkwqFqlGfLK5djMwV8CrsN0-tWib";
    private final String clientSecret = "EIOH8nYu2rv9-ZhFuD20-oK58JpQYvrdrSv_U1EfhJWEy_pE8H_ycYya-vZcsqukYj2jBvqK82Tirw1U";

    //setup paypal envrionment
    public PayPalEnvironment environment = new PayPalEnvironment.Sandbox(clientId, clientSecret);
    //Create client for environment
    public PayPalHttpClient client = new PayPalHttpClient(environment);
}*/