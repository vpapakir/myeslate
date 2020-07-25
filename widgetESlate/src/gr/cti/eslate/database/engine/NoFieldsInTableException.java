package gr.cti.eslate.database.engine;


public class NoFieldsInTableException extends Exception {
    public String message;
    public NoFieldsInTableException() {};
    public NoFieldsInTableException(String msg) {
        super(msg);
        message = msg;
    }
}
