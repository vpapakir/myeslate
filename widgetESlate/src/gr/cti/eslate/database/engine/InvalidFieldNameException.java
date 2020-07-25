package gr.cti.eslate.database.engine;


public class InvalidFieldNameException extends Exception {
    public String message;
    public InvalidFieldNameException() {};
    public InvalidFieldNameException(String msg) {
        super(msg);
        message = msg;
    }
}
