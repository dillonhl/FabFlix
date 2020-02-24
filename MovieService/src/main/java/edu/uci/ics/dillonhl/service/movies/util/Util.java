package edu.uci.ics.dillonhl.service.movies.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import edu.uci.ics.dillonhl.service.movies.MoviesService;
import edu.uci.ics.dillonhl.service.movies.base.ResponseModel;
import edu.uci.ics.dillonhl.service.movies.base.Result;
import edu.uci.ics.dillonhl.service.movies.logger.ServiceLogger;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Util
{
    private static final ObjectMapper MAPPER = new ObjectMapper();


    public static <T, S extends ResponseModel> T modelMapper(
            String jsonString, Class<T> className, S responseModel)
    {
        ServiceLogger.LOGGER.info("Mapping object from String");

        try {
            return MAPPER.readValue(jsonString, className);

        } catch (IOException e) {
            setException(e, responseModel);
        }

        //ServiceLogger.LOGGER.info("Mapping Object Failed: " + responseModel.getResult());

        return null;
    }

    private static <S extends ResponseModel> void setException(IOException e, S responseModel)
    {
        if (e instanceof JsonMappingException) {
           // responseModel.setResult(Result.JSON_MAPPING_EXCEPTION);

        } else if (e instanceof JsonParseException) {
            //responseModel.setResult(Result.JSON_PARSE_EXCEPTION);

        } else {
           // responseModel.setResult(Result.INTERNAL_SERVER_ERROR);

        }
    }

    public static <T> T modelMapper(String jsonString, Class<T> className)
    {
        ObjectMapper mapper = new ObjectMapper();

        ServiceLogger.LOGGER.info("Mapping object");

        try {
            return mapper.readValue(jsonString, className);

        } catch (IOException e) {
            ServiceLogger.LOGGER.info("Mapping Object Failed: " + e.getMessage());
            return null;

        }
    }

    public static PreparedStatement prepareStatement(String query, Param[] paramList)
            throws SQLException
    {
        ServiceLogger.LOGGER.info("Preparing Statement");

        int count = 1;

        PreparedStatement ps = MoviesService.getCon().prepareStatement(query);

        for (Param param : paramList)
            ps.setObject(count++, param.getParam(), param.getType());

        ServiceLogger.LOGGER.info("QueryReady: " + ps.toString());

        return ps;
    }

    public static Response serverError(String message)
    {
        ServiceLogger.LOGGER.info("Server ran into an error: " + message);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}