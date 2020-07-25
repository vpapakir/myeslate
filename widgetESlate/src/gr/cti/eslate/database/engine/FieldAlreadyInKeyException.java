package gr.cti.eslate.database.engine;


public class FieldAlreadyInKeyException extends Exception {
    public String message;
    public FieldAlreadyInKeyException() {};
    public FieldAlreadyInKeyException(String msg) {
        super(msg);
        message = msg;
    }
}

