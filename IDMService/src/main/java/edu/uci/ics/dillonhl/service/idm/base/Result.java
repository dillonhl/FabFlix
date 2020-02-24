package edu.uci.ics.dillonhl.service.idm.base;

import javax.ws.rs.core.Response.Status;

public enum Result
{
    JSON_PARSE_EXCEPTION   (-3, "JSON Parse Exception.",   Status.BAD_REQUEST),
    JSON_MAPPING_EXCEPTION (-2, "JSON Mapping Exception.", Status.BAD_REQUEST),

    INTERNAL_SERVER_ERROR  (-1, "Internal Server Error.",  Status.INTERNAL_SERVER_ERROR),

    PRIVILEGE_OUT_OF_RANGE  (-14, "Privilege level out of valid range.", Status.BAD_REQUEST),
    INVALID_TOKEN_LENGTH    (-13, "Token has invalid length.", Status.BAD_REQUEST),
    INVALID_PASSWORD_LENGTH (-12, "Password invalid length.", Status.BAD_REQUEST),
    INVALID_EMAIL_FORMAT    (-11, "Email address has invalid format.", Status.BAD_REQUEST),
    INVALID_EMAIL_LENGTH    (-10, "Email address has invalid length.", Status.BAD_REQUEST),
    PASSWORD_LENGTH_INCORRECT ( 12, "Password does not meet length requirements.", Status.OK),
    PASSWORD_CHAR_INCORRECT (13, "Password does not meet character requirements.", Status.OK),
    USER_NOT_FOUND          (14, "User not found.", Status.OK),
    EMAIL_IN_USE            (16, "Email already in use.", Status.OK),
    USER_REGISTERED_SUCCESS (110, "User registered successfully.", Status.OK),
    USER_LOGIN_SUCCESSFUL   (120, "User logged in successfully.", Status.OK),
    SESSION_ACTIVE          (130,"Session is active.", Status.OK),
    SESSION_EXPIRED         (131, "Session is expired.", Status.OK),
    SESSION_CLOSED          (132, "Session is closed.", Status.OK),
    SESSION_REVOKED         (133, "Session is revoked", Status.OK),
    SESSION_NOT_FOUND       (134, "Session not found.", Status.OK),
    USER_SUFFICIENT_PLEVEL  (140, "User has sufficient privilege level.", Status.OK),
    USER_INSUFFICIENT_PLEVEL(141, "User has insufficient privilege level.", Status.OK);

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
