package gr.cti.eslate.math.linalg.exceptions;


public class NonSquareMatrixException extends RuntimeException {
    public NonSquareMatrixException() {
        super();
    }

    public NonSquareMatrixException(String msg) {
        super(msg);
    }
}
