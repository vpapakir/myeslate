package gr.cti.eslate.math.linalg.exceptions;


public class SingularMatrixException extends RuntimeException {
    public SingularMatrixException() {
        super();
    }

    public SingularMatrixException(String msg) {
        super(msg);
    }
}
