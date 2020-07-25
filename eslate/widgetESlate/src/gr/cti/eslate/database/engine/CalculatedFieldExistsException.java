package gr.cti.eslate.database.engine;


public class CalculatedFieldExistsException extends Exception {
    public String message;
    public CalculatedFieldExistsException() {};
    public CalculatedFieldExistsException(String msg) {
        super(msg);
        message = msg;
    }
}
