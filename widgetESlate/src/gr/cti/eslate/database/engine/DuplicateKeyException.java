package gr.cti.eslate.database.engine;


public class DuplicateKeyException extends Exception {
    public String message;
    public DuplicateKeyException() {};
    public DuplicateKeyException(String msg) {
        super(msg);
        message = msg;
    }
}
