package gr.cti.eslate.database.engine;


public class FieldNameInUseException extends Exception {
    public String message;
    public FieldNameInUseException() {};
    public FieldNameInUseException(String msg) {
        super(msg);
        message = msg;
    }
}
