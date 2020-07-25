package gr.cti.eslate.database.engine;


public class InvalidOperatorException extends Exception {
    public String message;
    public InvalidOperatorException() {};
    public InvalidOperatorException(String msg) {
        super(msg);
        message = msg;
    }
}
