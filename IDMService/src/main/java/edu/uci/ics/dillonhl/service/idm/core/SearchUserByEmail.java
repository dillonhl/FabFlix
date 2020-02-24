package edu.uci.ics.dillonhl.service.idm.core;

import edu.uci.ics.dillonhl.service.idm.IDMService;
import edu.uci.ics.dillonhl.service.idm.logger.ServiceLogger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchUserByEmail {
    public static int searchPLevelByEmail(String email)
    {
        try {
            String query = "SELECT plevel" +
                    " FROM user" +
                    " WHERE email = ?;";

            PreparedStatement ps = IDMService.getCon().prepareStatement(query);
            ps.setString(1, email);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            if (rs.next() == false)
            {
                System.err.println("User not found.");
                return 0;

            }
            else
            {
                Integer plvl = rs.getInt("plevel");
                System.err.println("plevel: " + plvl);
                return plvl;
            }



        } catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve info.");
            e.printStackTrace();
            return 0;
        }
    }
}
