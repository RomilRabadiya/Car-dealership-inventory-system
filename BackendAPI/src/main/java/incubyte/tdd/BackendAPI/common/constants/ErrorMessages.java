package incubyte.tdd.BackendAPI.common.constants;

public final class ErrorMessages {

    private ErrorMessages(){}

    public static final String VEHICLE_NOT_FOUND =
            "Vehicle not found with id: ";

    public static final String OUT_OF_STOCK =
            "Vehicle is out of stock.";

    public static final String INVALID_QUANTITY =
            "Quantity must be greater than zero.";

    public static final String DUPLICATE_EMAIL =
            "Email already exists.";

}
