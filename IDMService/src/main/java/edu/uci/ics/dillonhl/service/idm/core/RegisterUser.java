package edu.uci.ics.dillonhl.service.idm.core;

import edu.uci.ics.dillonhl.service.idm.IDMService;
import edu.uci.ics.dillonhl.service.idm.logger.ServiceLogger;

import javax.xml.ws.Service;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterUser {
    public static void registerUserIntoDB(String email, String password, String salt)
    {
        try {
            System.err.println("Password in register: " + password);

            String query = "INSERT INTO user (email, status, plevel, salt, pword)" +
                            " VALUES (?, 1, 5, ?, ?);";

            PreparedStatement ps = IDMService.getCon().prepareStatement(query);

            ps.setObject(1, email);
            ps.setObject(2, salt);
            ps.setObject(3, password);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Query succeeded.");


        } catch (SQLException e)

        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to register user.");
            e.printStackTrace();
        }

    }
}
