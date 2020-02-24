package edu.uci.ics.dillonhl.service.billing.base;

import javax.ws.rs.core.Response.Status;

public enum Result
{
    JSON_PARSE_EXCEPTION (-3, "JSON Parse Exception.", Status.BAD_REQUEST),
    JSON_MAPPING_EXCEPTION (-2, "JSON Mapping Exception.", Status.BAD_REQUEST),

    INTERNAL_SERVER_ERROR (-1, "Internal Server Error.", Status.INTERNAL_SERVER_ERROR),

    USER_NOT_FOUND (14, "User not found.", Status.OK),
    INVALID_QUANTITY (33, "Quantity has invalid value", Status.OK),
    INSERT_IN_CART_SUCCESSFUL (3100, "Shopping cart item inserted successfully.", Status.OK),
    DUPLICATE_INSERTION (311, "Duplicate insertion.", Status.OK),
    NONEXISTENT_ITEM (312, "Shopping cart item does not exist.", Status.OK),
    NO_ORDER_HISTORY (313, "Order history does not exist.", Status.OK),
    ORDER_CREATION_FAILED (342, "Order creation failed.", Status.OK),
    UPDATE_CART_SUCCESSFUL (3110, "Shopping cart item updated successfully", Status.OK),
    DELETE_SUCCESSFUL (3120, "Shopping cart item deleted successfully.", Status.OK),
    RETRIEVED_SUCCESSFUL (3130, "Shopping cart retrieved successfully.", Status.OK),
    CLEARED_SUCCESSFUL (3140, "Shopping cart cleared successfully.", Status.OK),
    OPERATION_FAILED (3150, "Shopping cart operation failed.", Status.OK),
    ORDER_PLACED_SUCCESSFUL (3400, "Order placed successfully.", Status.OK),
    ORDER_RETRIEVED_SUCCESSFUL (3410, "Order retrieved successfully.", Status.OK),
    ORDER_COMPLETED_SUCCESSFUL (3420, "Order is completed successfully.", Status.OK),
    TOKEN_NOT_FOUND (3421, "Token not found.", Status.OK),
    ORDER_NOT_COMPLETED (3422, "Order can not be completed.", Status.OK)

    ;

    private final int resultCode;
    private final String message;
    private final Status status;

    Result(int resultCode, String message, Status status)
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

    public Status getStatus() {
        return status;
    }
}
