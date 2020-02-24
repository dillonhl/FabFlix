package edu.uci.ics.dillonhl.service.idm.core;

import edu.uci.ics.dillonhl.service.idm.IDMService;
import edu.uci.ics.dillonhl.service.idm.logger.ServiceLogger;
import edu.uci.ics.dillonhl.service.idm.security.Session;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class InsertSession {
    public static void insertNewSession(String session_id, String email)
    {
        try {
            String query = "INSERT INTO session (session_id, email, status, expr_time)" +
                                                  " VALUES (?, ?, 1, ?);";

            PreparedStatement ps = IDMService.getCon().prepareStatement(query);
            ps.setObject(1, session_id);
            ps.setObject(2, email);
            ps.setObject(3, new Date(System.currentTimeMillis() + Session.TOKEN_EXPR));
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Query succeeded.");



        } catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to register session.");
            e.printStackTrace();
        }

    }
}
