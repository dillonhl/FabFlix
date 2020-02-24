package edu.uci.ics.dillonhl.service.idm.core;

import edu.uci.ics.dillonhl.service.idm.IDMService;
import edu.uci.ics.dillonhl.service.idm.logger.ServiceLogger;
import edu.uci.ics.dillonhl.service.idm.security.Crypto;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserLogin {
    public static int login(String email, char[] password)
    {

        System.err.println("Email: " + email + "\nPassword: " + password);

        try {
            String que = "SELECT salt FROM user WHERE email = ?;";
            PreparedStatement saltPS = IDMService.getCon().prepareStatement(que);
            saltPS.setObject(1, email);
            ServiceLogger.LOGGER.info("Trying query: " + saltPS.toString());
            ResultSet saltRS = saltPS.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            // salt and hash
            String userSalt = "";
            if (saltRS.next()) {
                userSalt = saltRS.getString("salt");
                System.err.println("Salt: " + userSalt);
            }
            else
            {
                return -1;
            }
            System.err.println("Password in request model: " + password);
            byte[] salt = Hex.decodeHex(userSalt);
            byte[] hashedPassword = Crypto.hashPassword(password, salt, Crypto.ITERATIONS, Crypto.KEY_LENGTH);

            String encodedPassword = Hex.encodeHexString(hashedPassword);

            String query = "SELECT email, pword" +
                    " FROM user" +
                    " WHERE email = ? and pword = ?;";

            PreparedStatement ps = IDMService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ps.setObject(2, encodedPassword);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            rs.last();
            if (rs.getRow() < 1)
            {
                return 0;
            }
            else
            {
                String user = rs.getString("email");
                System.err.println("User: " + user);
                return 1;
            }




        } catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve info.");
            e.printStackTrace();
            return -1;
        } catch (DecoderException d)
        {
            ServiceLogger.LOGGER.warning("Failed to decode.");
            d.printStackTrace();
            return -1;
        }
    }
}
