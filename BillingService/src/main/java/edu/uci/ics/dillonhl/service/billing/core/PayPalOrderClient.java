package edu.uci.ics.dillonhl.service.billing.core;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;

public class PayPalOrderClient
{
    private final String clientId = "AS1njKCVkFkqGlS2nG-nyOZOuePnRbykD96YoUjzX__Zya0QuNuJGkwqFqlGfLK5djMwV8CrsN0-tWib";
    private final String clientSecret = "EIOH8nYu2rv9-ZhFuD20-oK58JpQYvrdrSv_U1EfhJWEy_pE8H_ycYya-vZcsqukYj2jBvqK82Tirw1U";

    //setup paypal envrionment
    public PayPalEnvironment environment = new PayPalEnvironment.Sandbox(clientId, clientSecret);
    //Create client for environment
    public PayPalHttpClient client = new PayPalHttpClient(environment);
}
