package gr.cti.eslate.database.engine;


public class InvalidFieldTypeException extends Exception {
    public String message;
    public InvalidFieldTypeException() {};
    public InvalidFieldTypeException(String msg) {
        super(msg);
        message = msg;
    }
}
