package incubyte.tdd.BackendAPI.Exception;

public class VehicleNotFoundException
        extends RuntimeException {

    public VehicleNotFoundException(String message) {
        super(message);
    }

}