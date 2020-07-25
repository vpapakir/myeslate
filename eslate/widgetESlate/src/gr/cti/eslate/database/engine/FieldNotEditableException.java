package gr.cti.eslate.database.engine;


public class FieldNotEditableException extends RuntimeException {
    public String message;
    public FieldNotEditableException() {};
    public FieldNotEditableException(String msg) {
        super(msg);
        message = msg;
    }
}
