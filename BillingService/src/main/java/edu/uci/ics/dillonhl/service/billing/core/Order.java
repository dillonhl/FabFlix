package edu.uci.ics.dillonhl.service.billing.core;

import edu.uci.ics.dillonhl.service.billing.BillingService;
import edu.uci.ics.dillonhl.service.billing.base.Result;
import edu.uci.ics.dillonhl.service.billing.logger.ServiceLogger;

import javax.xml.ws.Service;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Order {
    public static float getCartTotal(String email)
    {
        try {
            String query = "SELECT movie_id, quantity FROM cart" +
                            " WHERE email = ?;";

            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setObject(1, email);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            float total = 0;

            if (rs.next())
            {
                do {
                    String priceQuery = "SELECT unit_price, discount FROM movie_price" +
                                        " WHERE movie_id = ?;";
                    PreparedStatement pricePS = BillingService.getCon().prepareStatement(priceQuery);
                    pricePS.setObject(1, rs.getString("movie_id"));

                    ServiceLogger.LOGGER.info("Trying query: " + pricePS.toString());
                    ResultSet priceRS = pricePS.executeQuery();
                    ServiceLogger.LOGGER.info("Query succeeded.");
                    if (priceRS.next()) {
                        if (priceRS.getFloat("discount") != 0.0)
                            total += priceRS.getFloat("unit_price") * rs.getInt("quantity");
                        else
                            total += priceRS.getFloat("unit_price") * rs.getFloat("quantity");
                    }
                } while (rs.next());
            }

            ServiceLogger.LOGGER.info("Total Price in cart: " + total);
            return total;
        } catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to check item.");
            e.printStackTrace();
            return -1;
        }
    }

    public static boolean insertSaleAndTransaction(String email, String token)
    {
        try {
            String query = "SELECT movie_id, quantity FROM cart" +
                        " WHERE email = ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setObject(1, email);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            String movie_id = "";
            int quantity = 0;
            if (rs.next()) // change to while loop
            {
                movie_id = rs.getString("movie_id");
                quantity = rs.getInt("quantity");
            }


            String saleQuery = "INSERT INTO sale (sale_id, email, movie_id, quantity, sale_date)" +
                    " VALUES (default,?, ?, ?, ?);";
            PreparedStatement salePS = BillingService.getCon().prepareStatement(saleQuery, Statement.RETURN_GENERATED_KEYS);
            salePS.setObject(1, email);
            salePS.setObject(2, movie_id);
            salePS.setObject(3, quantity);
            salePS.setObject(4, new Date());

            ServiceLogger.LOGGER.info("Trying query: " + salePS.toString());
            salePS.execute();
            ServiceLogger.LOGGER.info("Query succeeded.");

            ResultSet saleRS = salePS.getGeneratedKeys();
            int sale_id = -1;
            if (saleRS.next()) {
                sale_id = saleRS.getInt(1);
                System.err.println("Sale_id : " + sale_id);
            }


            String transQuery = "INSERT INTO transaction (sale_id, token)" +
                                    " VALUES (?, ?);";
            PreparedStatement transPS = BillingService.getCon().prepareStatement(transQuery);
            transPS.setObject(1, sale_id);
            transPS.setObject(2, token);

            ServiceLogger.LOGGER.info("Trying query: " + transPS.toString());
            transPS.execute();
            ServiceLogger.LOGGER.info("Query succeeded.");

            return true;

        } catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to check item.");
            e.printStackTrace();
            return false;
        }
    }

    public static String checkToken(String token)
    {
        try {
            String query = "SELECT sale.email FROM sale LEFT JOIN transaction ON sale.sale_id = transaction.sale_id" +
                        " WHERE token = ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setObject(1, token);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            if (rs.next())
            {
                return rs.getString("email");
            }
            else
            {
                return null;
            }


        }catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to check item.");
            e.printStackTrace();
            return null;
        }
    }

    public static void updateTrans(String capture_id, String token)
    {
        try{
             String query = "UPDATE transaction SET capture_id = ?" +
                            " WHERE token = ?;";
             PreparedStatement ps = BillingService.getCon().prepareStatement(query);
             ps.setObject(1, capture_id);
             ps.setObject(2,token);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Query succeeded.");


        } catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to check item.");
            e.printStackTrace();

        }
    }

    public static String getOrderID(String email)
    {
        try
        {
            // select unique token ****
            String query = "SELECT token FROM transaction" +
                            " LEFT JOIN sale ON transaction.sale_id = sale.sale_id" +
                            " WHERE email = ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setObject(1,email);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            // change to do while loop
            if (rs.next()){
                return rs.getString("token");
            }
            else
            {
                return "Empty";
            }

        } catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to check item.");
            e.printStackTrace();
            return "Server Error";
        }
    }
}
