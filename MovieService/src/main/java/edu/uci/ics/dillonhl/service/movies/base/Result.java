package edu.uci.ics.dillonhl.service.movies.base;

import javax.ws.rs.core.Response.Status;

public enum Result
{
    JSON_PARSE_EXCEPTION   (-3, "JSON Parse Exception.",   Status.BAD_REQUEST),
    JSON_MAPPING_EXCEPTION (-2, "JSON Mapping Exception.", Status.BAD_REQUEST),

    INTERNAL_SERVER_ERROR  (-1, "Internal Server Error.",  Status.INTERNAL_SERVER_ERROR),

    FOUND_MOVIES            (210, "Found movie(s) with search parameters.", Status.OK),
    NO_MOVIE_FOUND          (211, "No movies found with search parameters.",    Status.OK),
    FOUND_PEOPLE             (212, "Found people with search parameters.", Status.OK),
    NO_PEOPLE_FOUND           (213, "No people found with search parameters.", Status.OK) ;

    private final int    resultCode;
    private final String message;
    private final Status status;

    Result(int resultCode, String message, Status status)
    {
        this.resultCode = resultCode;
        this.message = message;
        this.status = status;
    }

    public int getResultCode()
    {
        return resultCode;
    }

    public String getMessage()
    {
        return message;
    }

    public Status getStatus()
    {
        return status;
    }
}