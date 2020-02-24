package edu.uci.ics.dillonhl.service.idm.core;

import edu.uci.ics.dillonhl.service.idm.IDMService;
import edu.uci.ics.dillonhl.service.idm.logger.ServiceLogger;
import edu.uci.ics.dillonhl.service.idm.security.Session;

import java.lang.invoke.SerializedLambda;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SearchSession {
    public static int searchSession(String email) {
        try {
            String query = "SELECT status" +
                    " FROM session" +
                    " WHERE email = ?;";

            PreparedStatement ps = IDMService.getCon().prepareStatement(query);
            ps.setString(1, email);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            while (rs.next()) {
                Integer rsStatus = rs.getInt("status");
                if (rsStatus == 1) {
                    return 1;
                }
            }

            return 0;


        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve info.");
            e.printStackTrace();
            return 0;
        }
    }

    public static String searchSessionID(String email) {
        try {
            String query = "SELECT status, session_id" +
                    " FROM session" +
                    " WHERE email = ?;";

            PreparedStatement ps = IDMService.getCon().prepareStatement(query);
            ps.setString(1, email);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            while (rs.next()) {
                Integer rsStatus = rs.getInt("status");
                if (rsStatus == 1) {
                    return rs.getString("session_id");
                }
            }

            return rs.getString("session_id");

        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve info.");
            e.printStackTrace();
            return null;
        }
    }

    public static void updateSession(String email, String session_id, int newStatus) {
        try {
            String query = "UPDATE session" +
                    " SET status = ?" +
                    " WHERE email = ? and session_id = ?;";

            PreparedStatement ps = IDMService.getCon().prepareStatement(query);
            ps.setInt(1, newStatus);
            ps.setString(2, email);
            ps.setString(3, session_id);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Query succeeded.");


        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve info.");
            e.printStackTrace();
        }
    }

    public static int getSessionStatus(String session_id) {
        try {
            String query = "SELECT status, last_used, expr_time" +
                    " FROM session" +
                    " WHERE session_id = ?;";

            PreparedStatement ps = IDMService.getCon().prepareStatement(query);
            ps.setString(1, session_id);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            if (rs.next() == false)
            {
                System.err.println("ResultSet is empty.");
                return 0;
            }
            if (rs.getInt("status") == 1)
            {
                Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                int newStatus = 0;
                if (currentTime.getTime() > rs.getTimestamp("expr_time").getTime()) {
                    System.err.println("Session expired.");
                    newStatus = 3;
                }
                else if (currentTime.getTime() - rs.getTimestamp("last_used").getTime() > Session.SESSION_TIMEOUT) {
                System.err.println("Session revoked.");
                newStatus = 4;
                }
                else if (currentTime.getTime() - rs.getTimestamp("last_used").getTime() < Session.SESSION_TIMEOUT)
                {
                    if (rs.getTimestamp("expr_time").getTime() - currentTime.getTime() < Session.SESSION_TIMEOUT)
                    {
                        newStatus = 5;
                    }
                    else
                    {
                        newStatus = 1;
                    }
                }


                String newQuery = "UPDATE session SET" +
                        " status = ?, last_used = ? WHERE session_id = ?;";

                PreparedStatement newPS = IDMService.getCon().prepareStatement(newQuery);

                if (newStatus == 5)
                    newPS.setObject(1, 4); // 2 for revoked
                else
                    newPS.setObject(1, newStatus);

                newPS.setObject(2, currentTime);
                newPS.setObject(3, session_id);

                ServiceLogger.LOGGER.info("Trying query: " + newPS.toString());
                newPS.execute();
                ServiceLogger.LOGGER.info("Query succeeded.");

                return newStatus;
            }
            else {
                System.err.println("Status of session: " + rs.getInt("status"));
                return rs.getInt("status");
            }

        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve info.");
            e.printStackTrace();
            return 0;
        }
    }
}
