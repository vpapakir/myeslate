package gr.cti.eslate.database.engine;


public class NullTableKeyException extends Exception {
    public String message;
    public NullTableKeyException() {};
    public NullTableKeyException(String msg) {
        super(msg);
        message = msg;
    }
}
