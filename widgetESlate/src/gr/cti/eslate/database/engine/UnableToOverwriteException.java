package gr.cti.eslate.database.engine;


public class UnableToOverwriteException extends Exception {
    public String message;
    public UnableToOverwriteException() {};
    public UnableToOverwriteException(String msg) {
        super(msg);
        message = msg;
    }
}
