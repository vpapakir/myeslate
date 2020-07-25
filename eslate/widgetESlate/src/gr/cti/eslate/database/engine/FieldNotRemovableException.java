package gr.cti.eslate.database.engine;


public class FieldNotRemovableException extends Exception {
    public String message;
    public FieldNotRemovableException() {};
    public FieldNotRemovableException(String msg) {
        super(msg);
        message = msg;
    }
}
