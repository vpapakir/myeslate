package gr.cti.eslate.database.engine;


public class UnableToOpenException extends Exception {
    public String message;
    public UnableToOpenException() {};
    public UnableToOpenException(String msg) {
        super(msg);
        message = msg;
    }
}
