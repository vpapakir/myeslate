package gr.cti.eslate.database.engine;


public class InvalidFieldIndexException extends Exception {
    public String message;
    public InvalidFieldIndexException() {};
    public InvalidFieldIndexException(String msg) {
        super(msg);
        message = msg;
    }
}
