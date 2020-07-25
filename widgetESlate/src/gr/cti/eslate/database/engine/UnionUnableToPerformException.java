package gr.cti.eslate.database.engine;


public class UnionUnableToPerformException extends Exception {
    public String message;
    public UnionUnableToPerformException() {};
    public UnionUnableToPerformException(String msg) {
        super(msg);
        message = msg;
    }
}
