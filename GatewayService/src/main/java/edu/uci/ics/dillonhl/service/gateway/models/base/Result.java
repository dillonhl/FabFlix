package edu.uci.ics.dillonhl.service.gateway.models.base;

import javax.ws.rs.core.Response;

public enum Result
{
    JSON_PARSE_EXCEPTION (-3, "JSON Parse Exception.", Response.Status.BAD_REQUEST),
    JSON_MAPPING_EXCEPTION (-2, "JSON Mapping Exception.", Response.Status.BAD_REQUEST),

    INTERNAL_SERVER_ERROR (-1, "Internal Server Error.", Response.Status.INTERNAL_SERVER_ERROR),

    USER_NOT_FOUND (14, "User not found.", Response.Status.OK),
    INVALID_QUANTITY (33, "Quantity has invalid value", Response.Status.OK),
    INSERT_IN_CART_SUCCESSFUL (3100, "Shopping cart item inserted successfully.", Response.Status.OK),
    DUPLICATE_INSERTION (311, "Duplicate insertion.", Response.Status.OK),
    NONEXISTENT_ITEM (312, "Shopping cart item does not exist.", Response.Status.OK),
    NO_ORDER_HISTORY (313, "Order history does not exist.", Response.Status.OK),
    ORDER_CREATION_FAILED (342, "Order creation failed.", Response.Status.OK),
    UPDATE_CART_SUCCESSFUL (3110, "Shopping cart item updated successfully", Response.Status.OK),
    DELETE_SUCCESSFUL (3120, "Shopping cart item deleted successfully.", Response.Status.OK),
    RETRIEVED_SUCCESSFUL (3130, "Shopping cart retrieved successfully.", Response.Status.OK),
    CLEARED_SUCCESSFUL (3140, "Shopping cart cleared successfully.", Response.Status.OK),
    OPERATION_FAILED (3150, "Shopping cart operation failed.", Response.Status.OK),
    ORDER_PLACED_SUCCESSFUL (3400, "Order placed successfully.", Response.Status.OK),
    ORDER_RETRIEVED_SUCCESSFUL (3410, "Order retrieved successfully.", Response.Status.OK),
    ORDER_COMPLETED_SUCCESSFUL (3420, "Order is completed successfully.", Response.Status.OK),
    TOKEN_NOT_FOUND (3421, "Token not found.", Response.Status.OK),
    ORDER_NOT_COMPLETED (3422, "Order can not be completed.", Response.Status.OK),

    NO_CONTENT          (204, "Waiting for response...", Response.Status.NO_CONTENT)

    ;

    private final int resultCode;
    private final String message;
    private final Response.Status status;

    Result(int resultCode, String message, Response.Status status)
    {
        this.resultCode = resultCode;
        this.message = message;
        this.status = status;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }

    public Response.Status getStatus() {
        return status;
    }
}
