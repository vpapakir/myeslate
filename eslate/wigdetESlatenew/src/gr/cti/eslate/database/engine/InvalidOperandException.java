package gr.cti.eslate.database.engine;


public class InvalidOperandException extends Exception {
    public String message;
    public InvalidOperandException() {};
    public InvalidOperandException(String msg) {
        super(msg);
        message = msg;
    }
}
