package edu.uci.ics.dillonhl.service.billing.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.dillonhl.service.billing.BillingService;
import edu.uci.ics.dillonhl.service.billing.base.Result;
import edu.uci.ics.dillonhl.service.billing.base.ThumbnailModel;
import edu.uci.ics.dillonhl.service.billing.base.itemModel;
import edu.uci.ics.dillonhl.service.billing.configs.MoviesConfigs;
import edu.uci.ics.dillonhl.service.billing.logger.ServiceLogger;
import edu.uci.ics.dillonhl.service.billing.models.ThumbnailRequestModel;
import edu.uci.ics.dillonhl.service.billing.models.ThumbnailResponseModel;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Cart {
    public static Result insert(String email, String movie_id, int quantity)
    {
        try {
            String query = "INSERT INTO cart (email, movie_id, quantity)" +
                    " VALUES (?, ?, ?);";

            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setObject(1, email);
            ps.setObject(2,movie_id);
            ps.setObject(3,quantity);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Query succeeded.");

            return Result.INSERT_IN_CART_SUCCESSFUL;


        } catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to insert into cart.");
            e.printStackTrace();
            return Result.OPERATION_FAILED;
        }

    }

    public static boolean checkItem(String email, String movie_id)
    {
        try {
            String query = "SELECT * FROM cart" +
                            " WHERE email = ? AND movie_id = ?;";

            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setObject(1, email);
            ps.setObject(2,movie_id);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            if (rs.next()) {
                ServiceLogger.LOGGER.info("Item: " + email +  ", " + movie_id + " in cart.");
                return true;
            }
            else {
                ServiceLogger.LOGGER.info("Item not in cart.");
                return false;
            }


        }catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to check item.");
            e.printStackTrace();
            return false;
        }
    }

    public static Result updateItem(String email, String movie_id, int quantity)
    {
        try {
            String query = "UPDATE cart SET quantity = ?" +
                    " WHERE email = ? AND movie_id = ?;";

            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setObject(1, quantity);
            ps.setObject(2, email);
            ps.setObject(3, movie_id);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Query succeeded.");

            return Result.UPDATE_CART_SUCCESSFUL;
        } catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to update cart.");
            e.printStackTrace();
            return Result.OPERATION_FAILED;
        }
    }

    public static Result deleteItem(String email, String movie_id)
    {
        try {
        String query = "DELETE FROM cart" +
                " WHERE email = ? AND movie_id = ?;"; // check this query syntax

        PreparedStatement ps = BillingService.getCon().prepareStatement(query);
        ps.setObject(1, email);
        ps.setObject(2, movie_id);

        ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
        ps.execute();
        ServiceLogger.LOGGER.info("Query succeeded.");

        return Result.DELETE_SUCCESSFUL;
    }catch (SQLException e)
    {
        ServiceLogger.LOGGER.warning("Query failed: Unable to clear cart.");
        e.printStackTrace();
        return Result.OPERATION_FAILED;
    }
    }

    public static Result clearCart(String email) {
        try {
            String query = "SELECT email, movie_id, quantity" +
                    " FROM cart WHERE email = ?;";

            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setObject(1, email);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            if (!rs.next())
            {
                return Result.NONEXISTENT_ITEM;
            }

            String newQuery = "DELETE FROM cart" +
                                " WHERE email = ?;"; // check this query syntax

            PreparedStatement newPS = BillingService.getCon().prepareStatement(newQuery);
            newPS.setObject(1, email);

            ServiceLogger.LOGGER.info("Trying query: " + newPS.toString());
            newPS.execute();
            ServiceLogger.LOGGER.info("Query succeeded.");

            return Result.CLEARED_SUCCESSFUL;

        }catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to clear cart.");
            e.printStackTrace();
            return Result.OPERATION_FAILED;
        }
    }

    public static boolean checkCart(String email)
    {
        try {
            String query = "SELECT * FROM cart" +
                    " WHERE email = ?;";

            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setObject(1, email);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            if (rs.next()) {
                ServiceLogger.LOGGER.info("Cart available.");
                return true;
            }
            else {
                ServiceLogger.LOGGER.info("No items in cart.");
                return false;
            }


        }catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to check item.");
            e.printStackTrace();
            return false;
        }
    }

    public static itemModel[] retrieveCart(String email) // could make this return an array of items so that if items == 0, return 312 result
    {
        try {
            String query = "SELECT * FROM cart" +
                    " JOIN movie_price on cart.movie_id = movie_price.movie_id" +
                    " WHERE email = ?;";

            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setObject(1, email);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");
            itemModel[] itemModel = null;
            int count = 0;
            while (rs.next())
            {
                count++;
            }

            if (count <= 0)
            {
                return itemModel;
            }
            rs.beforeFirst();
            int i = 0;
            itemModel = new itemModel[count];
            String[] movie_ids = new String[count];
            while (rs.next()) {
                // insert into a list of items for the response model.
                System.err.println("Item: " +
                        rs.getString("email") + ", " +
                        rs.getString("movie_id"));
                itemModel[i] = new itemModel();
                itemModel[i].setEmail(rs.getString("email"));
                itemModel[i].setMovie_id(rs.getString("movie_id"));
                itemModel[i].setQuantity(rs.getInt("quantity"));
                itemModel[i].setDiscount(rs.getFloat("discount"));
                itemModel[i].setUnit_price(rs.getFloat("unit_price"));

                movie_ids[i] = rs.getString("movie_id");
                i++;
            }

            ThumbnailModel[] thumbnails = getThumbnails(movie_ids);

            for (int j = 0; j < count; j++)
            {
                System.err.println("Title from thumbnail: " + thumbnails[j].getTitle());
                itemModel[j].setMovie_title(thumbnails[j].getTitle());
                itemModel[j].setBackdrop_path(thumbnails[j].getBackdrop_path());
                itemModel[j].setPoster_path(thumbnails[j].getPoster_path());
            }

            System.err.println("RETRIEVE QUERY END");
            return itemModel;

        }catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve cart.");
            e.printStackTrace();
            return null;
        }
    }

    public static ThumbnailModel[] getThumbnails(String[] movie_ids)
    {
        ThumbnailRequestModel requestModel = new ThumbnailRequestModel(movie_ids);
        ThumbnailResponseModel responseModel;
        MoviesConfigs moviesConfigs = BillingService.getMoviesConfigs();
        String servicePath = moviesConfigs.getScheme() + moviesConfigs.getHostName() + ":" +
                                moviesConfigs.getPort() + moviesConfigs.getPath();
        String endpointPath = BillingService.getMoviesConfigs().getThumbnailPath();

        System.err.println("Full thumbnail path : " + servicePath + endpointPath);

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
        Response response = invocationBuilder.post(Entity.entity(requestModel, MediaType.APPLICATION_JSON));
        ServiceLogger.LOGGER.info("Request sent.");

        ServiceLogger.LOGGER.info("Received status " + response.getStatus());

        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonText = response.readEntity(String.class);
            System.err.println("jsonText in response: " + jsonText);
            responseModel = mapper.readValue(jsonText, ThumbnailResponseModel.class);
            ServiceLogger.LOGGER.info("Successfully mapped response.");
            System.err.println("Response model trying to get resultCode: " + responseModel.getResultCode() +
                    "\nMessage: " + responseModel.getMessage());
        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        ThumbnailModel[] thumbnails = responseModel.getThumbnails();

        return thumbnails;

    }

    public static boolean validItem(String movie_id) {
        try {
            String query = "SELECT * FROM movie_price" +
                    " WHERE movie_id = ?;";

            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setObject(1, movie_id);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            if (rs.next()) {
                ServiceLogger.LOGGER.info("Item available.");
                return true;
            }
            else {
                ServiceLogger.LOGGER.info("No price for item...");
                return false;
            }


        }catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to check item.");
            e.printStackTrace();
            return false;
        }

    }
}
