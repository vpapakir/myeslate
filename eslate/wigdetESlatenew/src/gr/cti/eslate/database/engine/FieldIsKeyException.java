package gr.cti.eslate.database.engine;


public class FieldIsKeyException extends Exception {
    public String message;
    public FieldIsKeyException() {};
    public FieldIsKeyException(String msg) {
        super(msg);
        message = msg;
    }
}
