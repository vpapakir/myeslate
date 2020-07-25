package gr.cti.eslate.database.engine;


public class InvalidFieldOperandException extends Exception {
    public String message;
    public InvalidFieldOperandException() {};
    public InvalidFieldOperandException(String msg) {
        super(msg);
        message = msg;
    }
}
