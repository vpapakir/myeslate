package gr.cti.eslate.database.engine;


public class InvalidPathException extends Exception {
    public String message;
    public InvalidPathException() {};
    public InvalidPathException(String msg) {
        super(msg);
        message = msg;
    }
}
