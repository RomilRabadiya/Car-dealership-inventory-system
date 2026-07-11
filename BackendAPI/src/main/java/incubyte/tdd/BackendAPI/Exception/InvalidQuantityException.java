package incubyte.tdd.BackendAPI.Exception;

public class InvalidQuantityException
        extends RuntimeException {

    public InvalidQuantityException(String message) {
        super(message);
    }

}