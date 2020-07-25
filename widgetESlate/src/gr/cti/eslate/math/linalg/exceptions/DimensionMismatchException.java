package gr.cti.eslate.math.linalg.exceptions;


public class DimensionMismatchException extends RuntimeException {
    public DimensionMismatchException() {
        super();
    }

    public DimensionMismatchException(String msg) {
        super(msg);
    }
}
