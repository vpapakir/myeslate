package gr.cti.eslate.database.engine;


public class FieldIsNotInKeyException extends Exception {
    public String message;
    public FieldIsNotInKeyException() {};
    public FieldIsNotInKeyException(String msg) {
        super(msg);
        message = msg;
    }
}
